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
package dk.nsi.sdm4.tilskudsblanket.recordspecs;

import dk.nsi.sdm4.core.persistence.recordpersister.RecordSpecification;

import static dk.nsi.sdm4.core.persistence.recordpersister.FieldSpecification.field;

/**
 * Specifikation af feltlænger og -navne for de forskellige balnkettyper
 */
public class TilskudsblanketRecordSpecs {
	
	public static final RecordSpecification BLANKET_RECORD_SPEC =
            RecordSpecification.createSpecification("Tilskudsblanket", "BlanketId",
                    field("BlanketId", 15, false).numerical(),
                    field("BlanketTekst", 21000, false));

    // Id is not present in input it should be a key created from
    // Genansoegning, Navn and Form
	public static final RecordSpecification BLANKET_ENKELTTILSKUD_RECORD_SPEC =
            RecordSpecification.createSpecification("TilskudsblanketEnkelt", "Id",
                    field("BlanketId", 15, false).numerical(),
                    field("Genansoegning", 1, false).numerical(),
                    field("Navn", 100, true),
                    field("Form", 100, true),
                    field("Id", 32, true));

    // DrugId is used as key because we should have a blanketId for each drugId
    // not the other way around.
	public static final RecordSpecification BLANKET_FORHOJETTILSKUD_RECORD_SPEC =
            RecordSpecification.createSpecification("TilskudsblanketForhoejet", "DrugId",
                    field("BlanketId", 15, false).numerical(),
                    field("DrugId", 12, true).numerical());

    // Genansogning is used as key even though it is an boolean
    // this is simply because we only have one blanket for each case
	public static final RecordSpecification BLANKET_KRONIKERTILSKUD_RECORD_SPEC =
            RecordSpecification.createSpecification("TilskudsblanketKroniker", "Genansoegning",
                    field("BlanketId", 15, false).numerical(),
                    field("Genansoegning", 1, false).numerical());

    // There is never more than one of these records, so we dont really have an Id
    // thats why we add an id column, it should always contain the same id
	public static final RecordSpecification BLANKET_TERMINALTILSKUD_RECORD_SPEC =
            RecordSpecification.createSpecification("TilskudsblanketTerminal", "Id",
                    field("BlanketId", 15, false).numerical(),
                    field("Id", 1, false).numerical());

	public static final RecordSpecification FORHOEJETTAKST_RECORD_SPEC =
            RecordSpecification.createSpecification("TilskudForhoejetTakst", "Varenummer",
                    field("Varenummer", 10, false).numerical(),
                    field("Navn", 30, false),
                    field("Form", 30, false),
                    field("FormTekst", 150, true),
                    field("ATCkode", 10, false),
                    field("Styrke", 30, true),
                    field("DrugID", 12, false).numerical(),
                    field("PakningsTekst", 30, false),
                    field("Udlevering", 10, false),
                    field("Tilskudstype", 10, false));
}
