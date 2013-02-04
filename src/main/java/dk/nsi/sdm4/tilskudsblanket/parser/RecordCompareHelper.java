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

import dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification;
import dk.nsi.sdm4.core.persistence.recordpersister.Record;
import dk.nsi.sdm4.core.persistence.recordpersister.RecordSpecification;

public class RecordCompareHelper {

    /**
     * Compare two records, taking optionals into account.
     * Records might not equal since optionals loaded from mysql contains null, but when parsed in they are not even
     * present in record.
     * @param spec
     * @param recordA
     * @param recordB
     * @return
     */
    public static boolean recordsEqualsFactoringInOptionals(RecordSpecification spec, Record recordA, Record recordB) {
        // If the normal equals returns true then they are definitely equals
        if (recordA.equals(recordB)) {
            return true;
        }
        boolean equals = true;
        for (FieldSpecification fieldSpec : spec.getFieldSpecs()) {
            Object oA = recordA.get(fieldSpec.name);
            Object oB = recordB.get(fieldSpec.name);
            if (oA == null || oB == null) {
                if (oA != null || oB != null) {
                    equals = false;
                    break;
                }
            } else {
                switch (fieldSpec.type) {
                    case ALPHANUMERICAL:
                        String sA = (String)oA;
                        String sB = (String)oB;
                        equals &= sA.equals(sB);
                        break;
                    case DECIMAL10_3:
                        Double dA = (Double)oA;
                        Double dB = (Double)oB;
                        equals &= dA.equals(dB);
                        break;
                    case NUMERICAL:
                        Long lA = (Long)oA;
                        Long lB = (Long)oB;
                        equals &= lA.equals(lB);
                }
            }
        }
        return equals;
    }

}
