package com.apriori.utils.pdf;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

@Slf4j
public class PDFDocument {

    private String documentContents;
    private int documentPageCount;

    public PDFDocument(String filePathName) {
        try {
            File pdfDocument = new File(filePathName);
            PDDocument doc = PDDocument.load(pdfDocument);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            this.documentContents = pdfStripper.getText(doc);
            this.documentPageCount = doc.getNumberOfPages();
            doc.close();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    public String getDocumentContents() {
        return documentContents;
    }

    public int getDocumentPageCount() {
        return documentPageCount;
    }
}
