package com.apriori.utils.email.response;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.excel.ExcelService;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.pdf.PDFDocument;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;

import java.io.File;

@Slf4j
@Data
@Schema(location = "EmailMessageAttachmentSchema.json")
public class EmailMessageAttachment {
    @JsonProperty("@odata.type")
    private String type;
    @JsonProperty("@odata.mediaContentType")
    private String mediaContentType;
    private String id;
    private String lastModifiedDateTime;
    private String name;
    private String contentType;
    private int size;
    @JsonProperty("isInline")
    private boolean inline;
    private String contentId;
    private Object contentLocation;
    private Object contentBytes;

    /**
     * Get Email File attachment from list of email message attachments
     *
     * @param <T> Response file type (PDFDocument or ExcelService class type)
     * @return PDFDocument or ExcelService depends on file type attached to the email
     */
    public <T> T getFileAttachment() {
        File downloadedFile = FileResourceUtil.copyIntoTempFile(getContentBytes().toString(), "reports", getName());
        final Object[] object = {null};
        switch (FileNameUtils.getExtension(String.valueOf(getName()))) {
            case "pdf":
                object[0] = new PDFDocument(String.valueOf(downloadedFile));
                break;
            case "xlsx":
                object[0] = new ExcelService(String.valueOf(downloadedFile));
                break;
            default:
                log.error(String.format("No attachments found !!"));
        }
        return (T) object[0];
    }
}
