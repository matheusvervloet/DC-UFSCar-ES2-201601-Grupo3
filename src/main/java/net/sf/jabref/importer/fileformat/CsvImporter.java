/*  Copyright (C) 2003-2015 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.jabref.importer.fileformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.jabref.Globals;
import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.model.entry.AuthorList;
import net.sf.jabref.model.entry.BibEntry;

/**
 * Imports a Biblioscape Tag File. The format is described on
 * http://www.biblioscape.com/manual_bsp/Biblioscape_Tag_File.htm Several
 * Biblioscape field types are ignored. Others are only included in the BibTeX
 * field "comment".
 */
public class CsvImporter extends ImportFormat {

    private static final Pattern RECOGNIZED_FORMAT_PATTERN = Pattern.compile("[^,\n]*(,[^,\n]*)*");

    /**
     * Return the name of this import format.
     */
    @Override
    public String getFormatName() {
        return "CSV";
    }

    /*
     *  (non-Javadoc)
     * @see net.sf.jabref.imports.ImportFormat#getCLIId()
     */
    @Override
    public String getCLIId() {
        return "csv";
    }

    /**
     * Check whether the source is in the correct format for this importer.
     */
    @Override
    public boolean isRecognizedFormat(InputStream stream) throws IOException {

        // Our strategy is to look for the "AU  - *" line.
        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {

            String str;
            while ((str = in.readLine()) != null) {
                if (RECOGNIZED_FORMAT_PATTERN.matcher(str).find()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Parse the entries in the source, and return a List of BibEntry
     * objects.
     */
    @Override
    public List<BibEntry> importEntries(InputStream stream, OutputPrinter status) throws IOException {
        List<BibEntry> bibitems = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
                sb.append('\n');
            }
        }

        String[] entries = sb.toString().split("\n");

        for (String entry1 : entries) {

            String type = "";
            String author = "";
            String editor = "";
            String comment = "";
            Map<String, String> hm = new HashMap<>();

            String[] fields = entry1.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            type = fields[0];

            String bibtexkey = fields[1];

            for (int j = 2; j < (fields.length - 1); j += 2) {
                String lab = fields[j];
                String val = fields[j + 1];

                if (val.endsWith("\"") && val.startsWith("\"")) {
                    val = val.substring(1, val.length() - 1);
                }

                if ("title".equals(lab)) {
                    String oldVal = hm.get("title");
                    if (oldVal == null) {
                        hm.put("title", val);
                    } else {
                        if (oldVal.endsWith(":") || oldVal.endsWith(".") || oldVal.endsWith("?")) {
                            hm.put("title", oldVal + " " + val);
                        } else {
                            hm.put("title", oldVal + ": " + val);
                        }
                    }
                    hm.put("title", hm.get("title").replaceAll("\\s+", " ")); // Normalize whitespaces
                } else if ("booktitle".equals(lab)) {
                    hm.put("booktitle", val);
                } else if ("series".equals(lab)) {
                    hm.put("series", val);
                } else if ("author".equals(lab)) {
                    if ("".equals(author)) {
                        author = val;
                    } else {
                        author += " and " + val;
                    }
                } else if ("editor".equals(lab)) {
                    if ("".equals(editor)) {
                        editor = val;
                    } else {
                        editor += " and " + val;
                    }
                } else if ("booktitle".equals(lab) || "journal".equals(lab)) {
                    if ("inproceedings".equals(type)) {
                        hm.put("booktitle", val);
                    } else {
                        hm.put("journal", val);
                    }
                } else if ("pages".equals(lab)) {
                    hm.put("pages", val);
                } else if ("school".equals(lab) || "publisher".equals(lab)) {
                    if ("phdthesis".equals(type)) {
                        hm.put("school", val);
                    } else {
                        hm.put("publisher", val);
                    }
                } else if ("address".equals(lab)) {
                    hm.put("address", val);
                } else if ("issn".equals(lab)) {
                    hm.put("issn", val);
                } else if ("volume".equals(lab)) {
                    hm.put("volume", val);
                } else if ("number".equals(lab)) {
                    hm.put("number", val);
                } else if ("abstract".equals(lab)) {
                    String oldAb = hm.get("abstract");
                    if (oldAb == null) {
                        hm.put("abstract", val);
                    } else {
                        hm.put("abstract", oldAb + Globals.NEWLINE + val);
                    }
                } else if ("url".equals(lab)) {
                    hm.put("url", val);
                } else if ("year".equals(lab)) {
                    hm.put("year", val);
                } else if ("keywords".equals(lab)) {
                    if (hm.containsKey("keywords")) {
                        String kw = hm.get("keywords");
                        hm.put("keywords", kw + ", " + val);
                    } else {
                        hm.put("keywords", val);
                    }
                } else if ("comment".equals(lab)) {
                    if (!comment.isEmpty()) {
                        comment = comment + " ";
                    }
                    comment = comment + val;
                } else if ("refid".equals(lab)) {
                    hm.put("refid", val);
                } else if ("doi".equals(lab)) {
                    hm.put("doi", val);
                } else if ("note".equals(lab)) {
                    hm.put("note", val);
                } else {
                    hm.put(lab, val);
                }
                // fix authors
                if (!author.isEmpty()) {
                    author = AuthorList.fixAuthorLastNameFirst(author);
                    hm.put("author", author);
                }
                if (!editor.isEmpty()) {
                    editor = AuthorList.fixAuthorLastNameFirst(editor);
                    hm.put("editor", editor);
                }
                if (!comment.isEmpty()) {
                    hm.put("comment", comment);
                }
            }
            BibEntry b = new BibEntry(DEFAULT_BIBTEXENTRY_ID, type); // id assumes an existing database so don't

            // Remove empty fields:
            List<Object> toRemove = new ArrayList<>();
            for (Map.Entry<String, String> key : hm.entrySet()) {
                String content = key.getValue();
                if ((content == null) || content.trim().isEmpty()) {
                    toRemove.add(key.getKey());
                }
            }
            for (Object aToRemove : toRemove) {
                hm.remove(aToRemove);

            }

            // create one here
            b.setField(hm);
            bibitems.add(b);

        }

        return bibitems;

    }
}
