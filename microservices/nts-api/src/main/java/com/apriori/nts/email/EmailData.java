package com.apriori.nts.email;

import com.apriori.nts.excel.ExcelService;
import com.apriori.nts.pdf.PDFDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.mail.Message;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailData {

    //region Fields
    private String subject = null;
    private Date sentDate = null;
    private String content = null;
    private List<File> attachments = null;
    private Message message = null;

    public <T> T getAttachments() {
        final Object[] object = {null};
        attachments.stream().forEach(fileName -> {
            switch (FileNameUtils.getExtension(String.valueOf(fileName))) {
                case "pdf":
                    object[0] = new PDFDocument(String.valueOf(fileName));
                    break;
                case "xlsx":
                    object[0] = new ExcelService(String.valueOf(fileName));
                    break;
                default:
                    log.error(String.format("No attachments found !!"));
            }
        });
        return (T) object[0];
    }

    public String setAttachments() {
        AtomicReference<String> reportName = new AtomicReference<>(StringUtils.EMPTY);
        attachments.stream().forEach(fileName -> {
            switch (FileNameUtils.getExtension(String.valueOf(fileName))) {
                default:
                    reportName.set(String.valueOf(fileName));

            }
        });
        return reportName.get();
    }

}
