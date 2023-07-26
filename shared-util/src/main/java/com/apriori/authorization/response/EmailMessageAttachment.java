package com.apriori.authorization.response;

import com.apriori.ExcelService;
import com.apriori.FileResourceUtil;
import com.apriori.PDFDocument;
import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;

import java.io.File;
import java.util.Objects;

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
     * @return DocumentType
     */
    public Object getFileAttachment() {
        Object object = null;
        File downloadedFile = FileResourceUtil.copyIntoTempFile(getContentBytes().toString(), "reports", getName());
        switch (FileNameUtils.getExtension(String.valueOf(getName()))) {
            case "pdf":
                object = new PDFDocument(String.valueOf(downloadedFile));
                break;
            case "xlsx":
                object = new ExcelService(String.valueOf(downloadedFile));
                break;
            default:
                log.error(String.format("No attachments found !!"));
        }
        if (Objects.isNull(object)) {
            throw new RuntimeException("Attachments Not Found!!!");
        }
        return object;
    }
}