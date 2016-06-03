package net.sf.jabref.es2test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImportItemTest {

    private BibtexImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new BibtexImporter();
    }

    @Test
    public void basicTest() throws IOException {
        try (InputStream stream = ImportItemTest.class.getResourceAsStream("es2bib.bib")) {
            List<BibEntry> entries = importer.importEntries(stream, new OutputPrinterToNull());

            assertEquals(2, entries.size());

            for (BibEntry entry : entries) {

                if (entry.getCiteKey().equals("small")) {
                    assertEquals("Freely, I.P.", entry.getField("author"));
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

