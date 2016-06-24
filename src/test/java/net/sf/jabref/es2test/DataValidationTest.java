package net.sf.jabref.es2test;

import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.BibtexEntryTypes;

import java.io.IOException;
import java.io.StringWriter;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.bibtex.BibEntryWriter;
import net.sf.jabref.exporter.LatexFieldFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataValidationTest {

    private BibEntry newEntryA;

    private BibEntryWriter writer;


    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        newEntryA = new BibEntry();
        newEntryA.setType(BibtexEntryTypes.ARTICLE);
        writer = new BibEntryWriter(new LatexFieldFormatter(), true);
    }

    @Test
    public void bibtexTestArticleYearEquals() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setField("year", "2003");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "  year = {2003}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void bibtexTestArticleYearNotEquals() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setField("year", "2003");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "  year = {2008}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        Assert.assertNotEquals(actual, expected);
    }

    @Test
    public void bibtexTestArticleYearLetter() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setField("year", "hello");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void bibtexTestArticleYearNegative() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setField("year", "-1");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void bibtexTestArticleYearEmpty() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setField("year", "");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);
    }

    @Test
    public void bibtexTestArticleKeyCorrect() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setCiteKey("key");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{key," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);

    }

    @Test
    public void bibtexTestArticleKeyTooSmall() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setCiteKey("k");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);

    }

    @Test
    public void bibtexTestArticleKeyEmpty() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setCiteKey("");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);

    }

    @Test
    public void bibtexTestArticleKeyNumber() {
        StringWriter stringWriter = new StringWriter();

        newEntryA.setCiteKey("123");

        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }

        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "}" + Globals.NEWLINE;

        Assert.assertEquals(actual, expected);

    }


}
