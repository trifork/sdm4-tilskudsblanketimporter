/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.nsi.sdm4.tilskudsblanket.parser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import dk.nsi.sdm4.core.persistence.recordpersister.*;
import dk.nsi.sdm4.tilskudsblanket.parser.enricher.EnkeltTilskudsRecordEnricher;
import dk.nsi.sdm4.tilskudsblanket.parser.enricher.RecordEnricher;
import dk.nsi.sdm4.tilskudsblanket.parser.enricher.TerminalTilskudsRecordEnricher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.nsi.sdm4.core.parser.Parser;
import dk.nsi.sdm4.core.parser.ParserException;
import dk.nsi.sdm4.tilskudsblanket.exception.InvalidTilskudsblanketDatasetException;
import dk.nsi.sdm4.tilskudsblanket.recordspecs.TilskudsblanketRecordSpecs;
import dk.sdsd.nsp.slalog.api.SLALogItem;
import dk.sdsd.nsp.slalog.api.SLALogger;

/**
 * Hoved-klasse i tilskudsblanketimporter-modulet. Ansvarlig for at koordinere importen af et datasæt af filer.
 * Antager at process-modetoden kaldes af ParserExecutor med et validt dataset
*/
public class TilskudsblanketParser implements Parser {
	private static final Logger log = Logger.getLogger(TilskudsblanketParser.class);

	@Autowired
	private SLALogger slaLogger;

	@Autowired
	private RecordPersister persister;

    @Autowired
    private RecordFetcher fetcher;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final Map<String, RecordSpecification> specsForFiles = new HashMap<String, RecordSpecification>() {
		{
			put("FT takster.txt", TilskudsblanketRecordSpecs.FORHOEJETTAKST_RECORD_SPEC);
			put("Blanket.txt", TilskudsblanketRecordSpecs.BLANKET_RECORD_SPEC);
			put("Blanketmap ET.txt", TilskudsblanketRecordSpecs.BLANKET_ENKELTTILSKUD_RECORD_SPEC);
			put("Blanketmap FT.txt", TilskudsblanketRecordSpecs.BLANKET_FORHOJETTILSKUD_RECORD_SPEC);
			put("Blanketmap KT.txt", TilskudsblanketRecordSpecs.BLANKET_KRONIKERTILSKUD_RECORD_SPEC);
			put("Blanketmap TT.txt", TilskudsblanketRecordSpecs.BLANKET_TERMINALTILSKUD_RECORD_SPEC);
		}
	};
    private final Map<String, RecordEnricher> enrichersForFiles = new HashMap<String, RecordEnricher>() {
        {
            put("Blanketmap ET.txt", new EnkeltTilskudsRecordEnricher());
            put("Blanketmap TT.txt", new TerminalTilskudsRecordEnricher());
        }
    };

	private static final String FILE_ENCODING = "ISO-8859-1";

	/**
	 * Ansvarlig for at håndtere import af et tilskudsblanket-datasæt.
	 * Antager, at den bliver kaldt i en kontekst hvor der allerede er etableret en transaktion.
	 * @see dk.nsi.sdm4.core.parser.Parser#getHome()
	 */
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void process(File datadir) throws ParserException {
		SLALogItem slaLogItem = slaLogger.createLogItem("TilskudsblanketParser", "All");

		validateDataset(datadir);

		try {
			assert datadir.listFiles() != null;
			for (File file : datadir.listFiles()) {
                String filename = file.getName();
                RecordSpecification spec = specsForFiles.get(filename);
				if (spec != null) {
                    RecordEnricher enricher = enrichersForFiles.get(filename);
					processSingleFile(file, spec, enricher);
				} else {
					// hvis vi ikke har nogen spec, skal filen ikke processeres.
					log.log(levelForUnexpectedFile(file), "Ignoring file " + file.getAbsolutePath());
				}
			}
		} catch (RuntimeException e) {
			slaLogItem.setCallResultError("TilskudsblanketParser failed - Cause: " + e.getMessage());
			slaLogItem.store();

			throw new ParserException(e);
		}


		slaLogItem.setCallResultOk();
		slaLogItem.store();
	}

	// kun ikke-private for at tillade test, kaldes ikke udefra
	Level levelForUnexpectedFile(File file) {
		Level logLevel = Level.WARN;
		return logLevel;
	}

	/**
	 * @throws InvalidTilskudsblanketDatasetException if a required file is missing or datadir is not a readable directory
	 */
	private void validateDataset(File datadir) {
		checkThat((datadir != null), "datadir is null");
		checkThat(datadir.isDirectory(), "datadir " + datadir.getAbsolutePath() + " is not a directory");
		checkThat(datadir.canRead(), "datadir " + datadir.getAbsolutePath() + " is not readable");

		Set<String> requiredFileNames = new HashSet<String>(specsForFiles.keySet());

		File[] actualFiles = datadir.listFiles();
		Set<String> actualFilenames = new HashSet<String>();
		for (File actualFile : actualFiles) {
			actualFilenames.add(actualFile.getName());
		}
		requiredFileNames.removeAll(actualFilenames);

		if (!requiredFileNames.isEmpty()) {
			throw new InvalidTilskudsblanketDatasetException("Missing files " + requiredFileNames + " from data directory " + datadir.getAbsolutePath());
		}
	}

	// kun ikke-private for at tillade test, kaldes ikke udefra
	void processSingleFile(File file, RecordSpecification spec, RecordEnricher enricher) {
		if (log.isDebugEnabled()) {
			log.debug("Processing file " + file + " with spec " + spec.getClass().getSimpleName());
		}
		SLALogItem slaLogItem = slaLogger.createLogItem("TilskudsblanketParser importer of file", file.getName());

		try {
			Set<Object> drugidsFromFile = parseAndPersistFile(file, spec, enricher);
			invalidateRecordsRemovedFromFile(drugidsFromFile, spec);
		} catch (RuntimeException e) {
			slaLogItem.setCallResultError("TilskudsblanketParser failed - Cause: " + e.getMessage());
			slaLogItem.store();

			throw new ParserException(e);
		}

		slaLogItem.setCallResultOk();
		slaLogItem.store();
	}

    /**
     * Parse and persist a file
     * @param file
     * @param spec
     * @param enricher
     * @return a set of object containing id's of records processed
     */
	private Set<Object> parseAndPersistFile(File file, RecordSpecification spec, RecordEnricher enricher) {
		DelimitedLineRecordParser recordParser = new DelimitedLineRecordParser(spec, ";", enricher);
		Set<Object> idsFromFile = new HashSet<Object>();

		List<String> lines = readFile(file);// files are very small, it's okay to hold them in memory
		if (log.isDebugEnabled()) {log.debug("Read " + lines.size() + " lines from file " + file.getAbsolutePath());}

		for (String line : lines) {
			if (log.isDebugEnabled()) log.debug("Processing line " + line);
			Record record = recordParser.parseLine(line);
			if (log.isDebugEnabled()) log.debug("Parsed line to record " + record);

			idsFromFile.add(record.get(spec.getKeyColumn()));
			persistRecordIfNeeeded(spec, record);
		}

		return idsFromFile;
	}

    /**
     * Read all lines from a file into a list
     * @param file
     * @return
     */
    private List<String> readFile(File file) {
		try {
			return FileUtils.readLines(file, FILE_ENCODING);
		} catch (IOException e) {
			throw new ParserException("Unable to read file " + file.getAbsolutePath(), e);
		}
	}

    /**
     * Persist a new record, if the record already exist and equals it is ignored.
     * @param spec
     * @param record
     */
	private void persistRecordIfNeeeded(RecordSpecification spec, Record record) {
		Record existingRecord = findRecordWithSameKey(record, spec);
		if (existingRecord != null) {
            if (RecordCompareHelper.recordsEqualsFactoringInOptionals(spec, existingRecord, record)) {
				// no need to do anything
				if (log.isDebugEnabled()) log.debug("Ignoring record " + record + " for spec " + spec.getTable() + " as we have identical record in db");
			} else {
				if (log.isDebugEnabled()) log.debug("Setting validTo on database record " + existingRecord + " for spec " + spec.getTable() + " before insertion of new record " + record);
                Date modifiedDate = persister.getTransactionTime().toDateTime().toDate();
                jdbcTemplate.update("UPDATE " + spec.getTable() + " SET ValidTo = ?, ModifiedDate = ? WHERE " + spec.getKeyColumn() + " = ? AND ValidTo IS NULL",
                        modifiedDate,
                        modifiedDate,
						existingRecord.get(spec.getKeyColumn()));
				persist(record, spec);
			}
		} else {
			if (log.isDebugEnabled()) log.debug("Persisting new record " + record + " for spec " + spec.getTable());
			persist(record, spec);
		}
	}

    /**
     * Tries to find a valid record in the database with same identifier as the record passed to the function
     * @param record
     * @param spec
     * @return the found record or null if it was not found
     */
	private Record findRecordWithSameKey(Record record, RecordSpecification spec) {
		try {
			return fetcher.fetchCurrent(record.get(spec.getKeyColumn())+"", spec);
		} catch (SQLException e) {
			throw new ParserException("While trying to find record with same key as " + record + " and spec " + spec, e);
		}
	}

    /**
     * Persist a record to database
     * @param record
     * @param spec
     */
	private void persist(Record record, RecordSpecification spec) {
		try {
			persister.persist(record, spec);
		} catch (SQLException e) {
			throw new ParserException("Unable to persist record " + record + " with spec " + spec, e);
		}
	}

	private void invalidateRecordsRemovedFromFile(Set<Object> idsFromFile, RecordSpecification spec) {
		// we'll compute the list of ids of record to be invalidated by fetching all the ids from the database, then weeding out all the ids that exist in the input file - these shouldn't be removed

        List<Object> idsOfAllRecords = fetchAllIdsFrom(spec);
        Set<Object> idsToBeInvalidated = new HashSet<Object>(idsOfAllRecords);
		idsToBeInvalidated.removeAll(idsFromFile);

		if (!idsToBeInvalidated.isEmpty()) {
            Date modifiedDate = persister.getTransactionTime().toDateTime().toDate();
            for (Object currentId : idsToBeInvalidated) {
                String sql = "UPDATE " + spec.getTable() + " SET ValidTo = ?, ModifiedDate = ? WHERE " +
                        spec.getKeyColumn() + "=? AND ValidTo IS NULL";
                jdbcTemplate.update(sql, modifiedDate, modifiedDate, currentId);
            }
		}
	}

    /**
     * Get a list containing all id's in a given table.
     * @param spec
     * @return
     */
    private List<Object> fetchAllIdsFrom(RecordSpecification spec) {
        FieldSpecification.RecordFieldType idType = null;
        for (FieldSpecification field : spec.getFieldSpecs()) {
            if (field.name.equals(spec.getKeyColumn())) {
                idType = field.type;
                break;
            }
        }
        assert idType != null;
        if (idType == FieldSpecification.RecordFieldType.ALPHANUMERICAL) {
            List<String> stringIds = jdbcTemplate.queryForList("SELECT " + spec.getKeyColumn() + " from " + spec.getTable() +
                    " WHERE ValidTo IS NULL", String.class);
            return new LinkedList<Object>(stringIds);
        } else if (idType == FieldSpecification.RecordFieldType.NUMERICAL) {
            List<Long> longIds = jdbcTemplate.queryForList("SELECT " + spec.getKeyColumn() + " from " + spec.getTable() +
                    " WHERE ValidTo IS NULL", Long.class);
            return new LinkedList<Object>(longIds);
        } else {
            throw new NotImplementedException("Only alphanumeric and numerical indexes implemented");
        }
    }


    /**
	 * @see dk.nsi.sdm4.core.parser.Parser#getHome()
	 */
	@Override
	public String getHome() {
		return "tilskudsblanketimporter";
	}

	private void checkThat(boolean expression, String errorMessage) {
		if (!expression) {
			throw new InvalidTilskudsblanketDatasetException(errorMessage);
		}
	}
}
