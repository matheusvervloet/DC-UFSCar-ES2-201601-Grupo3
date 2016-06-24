package net.sf.jabref.es2test;

import net.sf.jabref.model.database.BibDatabase;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.database.KeyCollisionException;
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

import static org.junit.Assert.*;

public class NewEntryTest {

    private BibEntry newEntryA;
    private BibEntry newEntryB;
    private BibEntryWriter writer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        newEntryA = new BibEntry();
        newEntryA.setType(BibtexEntryTypes.ARTICLE);
        newEntryB = new BibEntry();
        newEntryB.setType(BibtexEntryTypes.BOOK);
        writer = new BibEntryWriter(new LatexFieldFormatter(), true);
    }

    @Test
    public void basicTest() {
        newEntryA.setField("year", "2003");
        StringWriter stringWriter = new StringWriter();
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE + "  year = {2003}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        String actual = stringWriter.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticle() {
        StringWriter stringWriter = new StringWriter();
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleYear() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setField("year", "2003");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{,\n  year = {2003},\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleYear2() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setField("year", "2003");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{,\n  year = {2008},\n}\n";
        String actual = stringWriter.toString();
        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleYear3() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setField("year", "hello");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleYear4() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setField("year", "");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleEmptyStrings() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setField("author", "");
        newEntryA.setField("title", "");
        newEntryA.setField("journal", "");
        newEntryA.setField("bibtexkey", "");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleNonEmptyStrings() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setField("author", "ABC");
        newEntryA.setField("title", "DEF");
        newEntryA.setField("journal", "GHI");
        newEntryA.setField("bibtexkey", "JKL");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{JKL,\n  author  = {ABC},\n  title   = {DEF},\n  journal = {GHI},\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleKey() {
        StringWriter stringWriter = new StringWriter();
        newEntryA.setCiteKey("key");
        try {
            writer.write(newEntryA, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Article{key,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestArticleDuplicateKeys() {
        BibDatabase database = new BibDatabase();
        BibEntry newEntryA2 = new BibEntry();
        newEntryA2.setType(BibtexEntryTypes.ARTICLE);
        newEntryA.setCiteKey("key");
        newEntryA2.setCiteKey("key");
        database.insertEntry(newEntryA);
        try {
            database.insertEntry(newEntryA2);
        } catch (KeyCollisionException ex) {
            fail("should not get here...");
        }
        assertEquals(database.getEntryCount(), 2);
    }

    @Test
    public void bibtexTestArticleNonDuplicateKeys() {
        BibDatabase database = new BibDatabase();
        BibEntry newEntryA2 = new BibEntry();
        newEntryA2.setType(BibtexEntryTypes.ARTICLE);
        newEntryA.setCiteKey("key");
        newEntryA2.setCiteKey("key2");
        database.insertEntry(newEntryA);
        try {
            database.insertEntry(newEntryA2);
        } catch (KeyCollisionException ex) {
            fail("should not get here...");
        }
        assertEquals(database.getEntryCount(), 2);
    }

    @Test
    public void bibtexTestBook() {
        StringWriter stringWriter = new StringWriter();
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookYear() {
        StringWriter stringWriter = new StringWriter();
        newEntryB.setField("year", "2003");
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{,\n  year = {2003},\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookYear2() {
        StringWriter stringWriter = new StringWriter();
        newEntryB.setField("year", "2003");
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{,\n  year = {2008},\n}\n";
        String actual = stringWriter.toString();
        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookYear3() {
        StringWriter stringWriter = new StringWriter();
        newEntryB.setField("year", "hello");
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookYear4() {
        StringWriter stringWriter = new StringWriter();
        newEntryB.setField("year", "");
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookEmptyStrings() {
        StringWriter stringWriter = new StringWriter();
        newEntryB.setField("author", "");
        newEntryB.setField("title", "");
        newEntryB.setField("publisher", "");
        newEntryB.setField("editor", "");
        newEntryB.setField("bibtexkey", "");
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{,\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookNonEmptyStrings() {
        StringWriter stringWriter = new StringWriter();
        newEntryB.setField("title", "ABC");
        newEntryB.setField("publisher", "DEF");
        newEntryB.setField("author", "GHI");
        newEntryB.setField("editor", "JKL");
        newEntryB.setField("bibtexkey", "MNO");
        try {
            writer.write(newEntryB, stringWriter, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        String expected = "\n@Book{MNO,\n  title     = {ABC},\n  publisher = {DEF},\n  author    = {GHI},\n  editor    = {JKL},\n}\n";
        String actual = stringWriter.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void bibtexTestBookDuplicateKeys() {
        BibDatabase database = new BibDatabase();
        BibEntry newEntryB2 = new BibEntry();
        newEntryB2.setType(BibtexEntryTypes.BOOK);
        newEntryB.setCiteKey("key");
        newEntryB2.setCiteKey("key");
        database.insertEntry(newEntryB);
        try {
            database.insertEntry(newEntryB2);
        } catch (KeyCollisionException ex) {
            fail("should not get here...");
        }
        assertEquals(database.getEntryCount(), 2);
    }

    @Test
    public void bibtexTestBookNonDuplicateKeys() {
        BibDatabase database = new BibDatabase();
        BibEntry newEntryB2 = new BibEntry();
        newEntryB2.setType(BibtexEntryTypes.BOOK);
        newEntryB.setCiteKey("key");
        newEntryB2.setCiteKey("key2");
        database.insertEntry(newEntryB);
        try {
            database.insertEntry(newEntryB2);
        } catch (KeyCollisionException ex) {
            fail("should not get here...");
        }
        assertEquals(database.getEntryCount(), 2);
    }
}
