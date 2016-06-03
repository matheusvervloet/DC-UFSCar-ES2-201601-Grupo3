package net.sf.jabref.es2test;

import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.BibtexEntryTypes;

import java.io.IOException;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.gui.entryeditor.EntryEditor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NewEntryTest {

    private BibEntry newEntry;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        newEntry = new BibEntry();
        newEntry.setType(BibtexEntryTypes.ARTICLE);
    }

    @Test
    public void basicTest() {
        newEntry.setField("year", "2003");
        Assert.assertEquals("2003", newEntry.getFieldOrAlias("year"));
    }

    @Test
    public void bibtexTest() {
        String srcString = new String();
        try {
            srcString = EntryEditor.getSourceString(newEntry, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n}", srcString);
    }

    @Test
    public void bibtexTestYear() {
        String srcString = new String();
        newEntry.setField("year", "2003");
        try {
            srcString = EntryEditor.getSourceString(newEntry, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n  year = {2003},\n}", srcString);
    }

    @Test
    public void bibtexTestYear2() {
        String srcString = new String();
        newEntry.setField("year", "2003");
        try {
            srcString = EntryEditor.getSourceString(newEntry, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertNotEquals("@Article{,\n  year = {2008},\n}", srcString);
    }

    @Test
    public void bibtexTestYear3() {
        String srcString = new String();
        newEntry.setField("year", "hello");
        try {
            srcString = EntryEditor.getSourceString(newEntry, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n  year = {hello},\n}", srcString);
    }

    @Test
    public void bibtexTestYear4() {
        String srcString = new String();
        newEntry.setField("year", "");
        try {
            srcString = EntryEditor.getSourceString(newEntry, BibDatabaseMode.BIBTEX);
        } catch (IOException e) {
        }
        Assert.assertEquals("@Article{,\n}", srcString);
    }

}
