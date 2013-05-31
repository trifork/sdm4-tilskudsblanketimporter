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

import dk.nsi.sdm4.testutils.TestDbConfiguration;
import dk.nsi.sdm4.tilskudsblanket.config.TilskudsblanketimporterApplicationConfig;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {TilskudsblanketimporterApplicationConfig.class, TestDbConfiguration.class})
/**
 * Denne test tester import af hele datasets
 */
public class TilskudsblanketParserFilesetTest {

    @Autowired
    private TilskudsblanketParser parser;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void processCompleteDataset() {
        File fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/set1"));
        parser.process(fileSet, "");
    }

    @Test
    public void processDatasetTwice() {
        File fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/set1"));
        parser.process(fileSet, "");
        int recordCount1 = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketEnkelt");

        fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/set1"));
        parser.process(fileSet, "");
        int recordCount2 = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketEnkelt");

        // We should not get any new records since they are all the same just imported twice
        assertEquals(recordCount1, recordCount2);
    }

    @Test
    public void importTwoDifferentDatasets() {
        File fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/set1"));
        parser.process(fileSet, "");
        int recordCount1 = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM TilskudsblanketEnkelt");
        fileSet = FileUtils.toFile(getClass().getClassLoader().getResource("data/set2"));
        parser.process(fileSet, "");
    }

}
