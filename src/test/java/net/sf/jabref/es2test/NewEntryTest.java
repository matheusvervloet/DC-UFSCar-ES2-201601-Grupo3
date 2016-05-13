package net.sf.jabref.es2test;

import net.sf.jabref.model.entry.BibEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NewEntryTest {

    private BibEntry newEntry;

    @Before
    public void setUp() {
        newEntry = new BibEntry();
    }

    @Test
    public void basicTest() {
        newEntry.setField("year", "2003");
        Assert.assertEquals("2003", newEntry.getFieldOrAlias("year"));
    }

}
