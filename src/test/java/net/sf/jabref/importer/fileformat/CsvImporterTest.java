package net.sf.jabref.importer.fileformat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CsvImporterTest {

    private CsvImporter csvImporter;


    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        csvImporter = new CsvImporter();
    }

    @Test
    public void testGetFormatName() {
        Assert.assertEquals(csvImporter.getFormatName(), "CSV");
    }

    @Test
    public void testGetCLIId() {
        Assert.assertEquals(csvImporter.getCLIId(), "csv");
    }

    @Test
    public void testIfNotRecognizedFormat() throws IOException {
        try (InputStream stream = CsvImporterTest.class.getResourceAsStream("es2csv-corrupted.csv")) {
            Assert.assertFalse(csvImporter.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testIfRecognizedFormat() throws IOException {
        try (InputStream stream = CsvImporterTest.class.getResourceAsStream("es2csv.csv")) {
            Assert.assertTrue(csvImporter.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testCsvFields() throws IOException {
        try (InputStream stream = CsvImporterTest.class.getResourceAsStream("es2csv.csv")) {
            List<BibEntry> entries = csvImporter.importEntries(stream, null);

            for (BibEntry entry : entries) {
                System.out.println(entry);
                if (entry.getCiteKey().equals("small")) {
                    assertEquals("Freely, I. P.", entry.getField("author"));
                    assertEquals("A small paper", entry.getField("title"));
                    assertEquals("The journal of small papers", entry.getField("journal"));
                    assertEquals("1997", entry.getField("year"));
                    assertEquals("-1", entry.getField("volume"));
                    assertEquals("to appear", entry.getField("note"));
                } else if (entry.getCiteKey().equals("big")) {
                    assertEquals("Jass, Hugh", entry.getField("author"));
                    assertEquals("A big paper", entry.getField("title"));
                    assertEquals("The journal of big papers", entry.getField("journal"));
                    assertEquals("7991", entry.getField("year"));
                    assertEquals("MCMXCVII", entry.getField("volume"));
                }
            }

        }
    }

}
