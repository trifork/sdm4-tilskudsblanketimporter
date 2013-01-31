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

import static dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification.RecordFieldType.ALPHANUMERICAL;
import static dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification.RecordFieldType.DECIMAL10_3;
import static dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification.RecordFieldType.NUMERICAL;

import dk.nsi.sdm4.core.persistence.recordpersister.Record;
import dk.nsi.sdm4.tilskudsblanket.parser.enricher.RecordEnricher;
import org.apache.commons.lang.StringUtils;

import dk.nsi.sdm4.core.parser.ParserException;
import dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordBuilder;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordSpecification;

public class DelimitedLineRecordParser {

    private static String NULL_INDICATOR = "<null>";

    private final RecordSpecification recordSpecification;
    private final String delimiter;
    private final RecordEnricher recordEnricher;

    public DelimitedLineRecordParser(RecordSpecification recordSpecification,
                                     String delimiter, RecordEnricher recordEnricher) {
        this.recordSpecification = recordSpecification;
        this.delimiter = delimiter;
        this.recordEnricher = recordEnricher;
    }

    public Record parseLine(String line) {
        RecordBuilder builder = new RecordBuilder(recordSpecification);

        try {
            String[] values = line.split(delimiter);

            builder = parseAsArray(values, line, builder);
            if (recordEnricher != null) {
                builder = recordEnricher.enrichRecord(values, builder);
            }
        } catch(Exception e) {
        	if(e instanceof ParserException) {
        		throw (ParserException)e;
        	} else {
                throw new ParserException("Parsing of line ["+line+"] with delimiter ["+delimiter+"] went wrong with error:" + e.getMessage());
        	}
        }

        return builder.build();
    }

    private RecordBuilder parseAsArray(String[] values, String line, RecordBuilder builder) {
        int offset = 0;
        for (FieldSpecification fieldSpecification : recordSpecification.getFieldSpecs()) {
            if (!fieldSpecification.ignored) {
                String subString;
                try {
                    subString = values[offset];
                } catch(ArrayIndexOutOfBoundsException e) {
                    // TODO: better errorhandling if this actually is an error (e.g line is to short...)
                    break;
                }
                String trimmedValue = subString.trim();
                if (!trimmedValue.equals(NULL_INDICATOR)) {
                    if (fieldSpecification.type == ALPHANUMERICAL) {
                        builder.field(fieldSpecification.name, trimmedValue);
                    } else if (fieldSpecification.type == DECIMAL10_3) {
                        if (StringUtils.isEmpty(trimmedValue)) {
                            throw new ParserException("Field " + fieldSpecification.name + " at offset " + offset +
                                    " in line " + line  + " has value '" + subString + "' which is not allowed for numerical fields");
                        }
                        builder.field(fieldSpecification.name, Double.parseDouble(trimmedValue));
                    } else if (fieldSpecification.type == NUMERICAL) {
                        if (StringUtils.isEmpty(trimmedValue) || !StringUtils.isNumeric(trimmedValue)) {
                            throw new ParserException("Field " + fieldSpecification.name + " at offset " + offset +
                                    " in line " + line  + " has value '" + subString + "' which is not allowed for numerical fields");
                        }
                        builder.field(fieldSpecification.name, Long.parseLong(trimmedValue));
                    } else {
                        throw new AssertionError("Should match exactly one of the types alphanumerical or numerical.");
                    }
                }
            }
            offset++;
        }
        return builder;
    }

}