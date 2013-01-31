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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import dk.nsi.sdm4.tilskudsblanket.parser.enricher.EnkeltTilskudsRecordEnricher;
import dk.nsi.sdm4.tilskudsblanket.parser.enricher.RecordEnricher;
import dk.nsi.sdm4.tilskudsblanket.parser.enricher.TerminalTilskudsRecordEnricher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import dk.nsi.sdm4.core.persistence.recordpersister.RecordPersister;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordSpecification;
import dk.nsi.sdm4.testutils.TestDbConfiguration;
import dk.nsi.sdm4.tilskudsblanket.config.TilskudsblanketimporterApplicationConfig;
import dk.nsi.sdm4.tilskudsblanket.recordspecs.TilskudsblanketRecordSpecs;

/**
 * Test af opførsel vedr validering af enkeltfiler.
 * Er en integrationstest, dvs. importeren gemmer i databasen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {TilskudsblanketimporterApplicationConfig.class, TestDbConfiguration.class})
public class TilskudsblanketParserSinglefilesIntegrationTest
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TilskudsblanketParser parser;

	@Autowired
	private RecordPersister persister;

	@Test
	public void persistsOptionalData() throws Exception {
	    importFile("optionalDataTest/Blanketmap ET.txt", TilskudsblanketRecordSpecs.BLANKET_ENKELTTILSKUD_RECORD_SPEC,
                new EnkeltTilskudsRecordEnricher());

		assertEquals(2, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketEnkelt"));
		assertNull(jdbcTemplate.queryForObject(
                "SELECT Navn FROM TilskudsblanketEnkelt WHERE blanketid = 9699940 AND ValidTo IS NULL", String.class));
	}

	@Test
	public void persistsEnkeltBlanketter() throws Exception {
	    importFile("data/set1/Blanketmap ET.txt", TilskudsblanketRecordSpecs.BLANKET_ENKELTTILSKUD_RECORD_SPEC,
                new EnkeltTilskudsRecordEnricher());

		assertEquals(197, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketEnkelt WHERE ValidTo IS NULL"));
	}

	@Test
	public void persistsForhoejetBlanketter() throws Exception {
	    importFile("data/set1/Blanketmap FT.txt", TilskudsblanketRecordSpecs.BLANKET_FORHOJETTILSKUD_RECORD_SPEC, null);

		assertEquals(1, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketForhoejet WHERE ValidTo IS NULL"));
	}

	@Test
	public void persistsKronikerBlanketter() throws Exception {
	    importFile("data/set1/Blanketmap KT.txt", TilskudsblanketRecordSpecs.BLANKET_KRONIKERTILSKUD_RECORD_SPEC, null);

		assertEquals(2, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketKroniker WHERE ValidTo IS NULL"));
	}

	@Test
	public void persistsTerminalBlanketter() throws Exception {
	    importFile("data/set1/Blanketmap TT.txt", TilskudsblanketRecordSpecs.BLANKET_TERMINALTILSKUD_RECORD_SPEC,
                new TerminalTilskudsRecordEnricher());

		assertEquals(1, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketTerminal WHERE ValidTo IS NULL"));
	}

	@Test
	public void persistsBlanketter() throws Exception {
	    importFile("data/set1/Blanket.txt", TilskudsblanketRecordSpecs.BLANKET_RECORD_SPEC, null);

		assertEquals(36, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM Tilskudsblanket WHERE ValidTo IS NULL"));
	}

	@Test
	public void persistsForhoejetTakst() throws Exception {
	    importFile("data/set1/FT Takster.txt", TilskudsblanketRecordSpecs.FORHOEJETTAKST_RECORD_SPEC, null);
		assertEquals(3550, jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudForhoejetTakst WHERE ValidTo IS NULL"));
	}

	private void importFile(String filePath, RecordSpecification spec, RecordEnricher enricher) throws Exception {
		parser.processSingleFile(new ClassPathResource(filePath).getFile(), spec, enricher);
	}

}
