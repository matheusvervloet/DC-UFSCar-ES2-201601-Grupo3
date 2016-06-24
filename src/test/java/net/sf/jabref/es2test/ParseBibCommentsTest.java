package net.sf.jabref.es2test;

import java.io.IOException;
import java.io.StringReader;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.ParserResult;
import net.sf.jabref.importer.fileformat.BibtexImporter;
import net.sf.jabref.importer.fileformat.BibtexParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseBibCommentsTest {

    private BibtexImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
    }

    @Test
    public void fromStringRecognizesComment() {
        ParserResult result = null;
        try {
            result = BibtexParser.parse(new StringReader("%comment@article{test,author={Ed von Test}}"));
        } catch (IOException e) {
        }

        assertEquals(result.getDatabase().getEpilog(), "%comment");
    }

    @Test
    public void fromStringNotRecognizesComment() {
        ParserResult result = null;
        try {
            result = BibtexParser.parse(new StringReader("%comment@article{test,author={Ed von Test}}"));
        } catch (IOException e) {
        }

        assertNotEquals(result.getDatabase().getEpilog(), "%comment@article");
    }

    @Test
    public void fromStringRecognizesMultipleComments() {
        ParserResult result = null;
        try {
            result = BibtexParser.parse(new StringReader("%comment\n%comment@article{test,author={Ed von Test}}"));
        } catch (IOException e) {
        }

        assertEquals(result.getDatabase().getEpilog(), "%comment\n%comment");
    }

    @Test
    public void fromStringEmptyComment() {
        ParserResult result = null;
        try {
            result = BibtexParser.parse(new StringReader("@article{test,author={Ed von Test}}"));
        } catch (IOException e) {
        }

        assertEquals(result.getDatabase().getEpilog(), "");
    }
}

