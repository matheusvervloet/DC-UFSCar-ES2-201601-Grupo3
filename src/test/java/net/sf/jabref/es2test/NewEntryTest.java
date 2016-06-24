package net.sf.jabref.es2test;

import net.sf.jabref.model.database.BibDatabase;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.database.KeyCollisionException;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.BibtexEntryTypes;

import java.io.IOException;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.gui.entryeditor.EntryEditor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewEntryTest {

    private BibEntry newEntryA;
    private BibEntry newEntryB;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        newEntryA = new BibEntry();
        newEntryA.setType(BibtexEntryTypes.ARTICLE);
        newEntryB = new BibEntry();
        newEntryB.setType(BibtexEntryTypes.BOOK);
    }

    @Test
    public void basicTest() {
        newEntryA.setField("year", "2003");
        Assert.assertEquals("2003", newEntryA.getFieldOrAlias("year"));
    }

    @Test
    public void bibtexTestArticle() {
        String srcString = new String();
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n}", srcString);
    }

    @Test
    public void bibtexTestArticleYear() {
        String srcString = new String();
        newEntryA.setField("year", "2003");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n  year = {2003},\n}", srcString);
    }

    @Test
    public void bibtexTestArticleYear2() {
        String srcString = new String();
        newEntryA.setField("year", "2003");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertNotEquals("@Article{,\n  year = {2008},\n}", srcString);
    }

    @Test
    public void bibtexTestArticleYear3() {
        String srcString = new String();
        newEntryA.setField("year", "hello");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n  year = {hello},\n}", srcString);
    }

    @Test
    public void bibtexTestArticleYear4() {
        String srcString = new String();
        newEntryA.setField("year", "");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n}", srcString);
    }

    @Test
    public void bibtexTestArticleEmptyStrings() {
        String srcString = new String();
        newEntryA.setField("author", "");
        newEntryA.setField("title", "");
        newEntryA.setField("journal", "");
        newEntryA.setField("bibtexkey", "");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n}", srcString);
    }

    @Test
    public void bibtexTestArticleNonEmptyStrings() {
        String srcString = new String();
        newEntryA.setField("author", "ABC");
        newEntryA.setField("title", "DEF");
        newEntryA.setField("journal", "GHI");
        newEntryA.setField("bibtexkey", "JKL");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{JKL,\n  author  = {ABC},\n  title   = {DEF},\n  journal = {GHI},\n}", srcString);
    }

    @Test
    public void bibtexTestArticleKey() {
        String srcString = new String();
        newEntryA.setCiteKey("key");
        try {
            srcString = EntryEditor.getSourceString(newEntryA, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{key,\n}", srcString);
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
        String srcString = new String();
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Book{,\n}", srcString);
    }

    @Test
    public void bibtexTestBookYear() {
        String srcString = new String();
        newEntryB.setField("year", "2003");
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Book{,\n  year = {2003},\n}", srcString);
    }

    @Test
    public void bibtexTestBookYear2() {
        String srcString = new String();
        newEntryB.setField("year", "2003");
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertNotEquals("@Book{,\n  year = {2008},\n}", srcString);
    }

    @Test
    public void bibtexTestBookYear3() {
        String srcString = new String();
        newEntryB.setField("year", "hello");
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Book{,\n  year = {hello},\n}", srcString);
    }

    @Test
    public void bibtexTestBookYear4() {
        String srcString = new String();
        newEntryB.setField("year", "");
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Book{,\n}", srcString);
    }

    @Test
    public void bibtexTestBookEmptyStrings() {
        String srcString = new String();
        newEntryB.setField("author", "");
        newEntryB.setField("title", "");
        newEntryB.setField("publisher", "");
        newEntryB.setField("editor", "");
        newEntryB.setField("bibtexkey", "");
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Book{,\n}", srcString);
    }

    @Test
    public void bibtexTestBookNonEmptyStrings() {
        String srcString = new String();
        newEntryB.setField("title", "ABC");
        newEntryB.setField("publisher", "DEF");
        newEntryB.setField("author", "GHI");
        newEntryB.setField("editor", "JKL");
        newEntryB.setField("bibtexkey", "MNO");
        try {
            srcString = EntryEditor.getSourceString(newEntryB, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals(
                "@Book{MNO,\n  title     = {ABC},\n  publisher = {DEF},\n  author    = {GHI},\n  editor    = {JKL},\n}",
                srcString);
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
