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
package dk.nsi.sdm4.tilskudsblanket.parser.enricher;

import dk.nsi.sdm4.core.persistence.recordpersister.RecordBuilder;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EnkeltTilskudsRecordEnricher implements RecordEnricher {

    /**
     * BLANKET_ENKELTTILSKUD_RECORD needs an identifer that is buildt using a combination of existing fields
     * @param parsedRecord
     * @param builder
     * @return
     */
    @Override
    public RecordBuilder enrichRecord(String[] parsedRecord, RecordBuilder builder) {
        // Extract Genansoegning, Navn and Form from the record and build an identifier from it.

        String genansoegning = parsedRecord.length > 1 ? parsedRecord[1] : "";
        String navn = parsedRecord.length > 2 ? parsedRecord[2] : "";
        String form = parsedRecord.length > 3 ? parsedRecord[3] : "";
        String md5Identifier = makeMd5Identifier(genansoegning + "-" + navn + "-" + form);
        builder.field("Id", md5Identifier);
        return builder;
    }

    private String makeMd5Identifier(String identifier) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(identifier.getBytes());
            result = String.valueOf(Hex.encodeHex(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
