package com.apriori.utils.email.response;

import com.apriori.utils.email.EmailConnection;
import com.apriori.utils.email.EmailEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    @JsonProperty("@odata.etag")
    public String etag;
    public String id;
    public String createdDateTime;
    public String lastModifiedDateTime;
    public String changeKey;
    public ArrayList<Object> categories;
    public String receivedDateTime;
    public String sentDateTime;
    public boolean hasAttachments;
    public String internetMessageId;
    public String subject;
    public String bodyPreview;
    public String importance;
    public String parentFolderId;
    public String conversationId;
    public String conversationIndex;
    public Object isDeliveryReceiptRequested;
    public boolean isReadReceiptRequested;
    public boolean isRead;
    public boolean isDraft;
    public String webLink;
    public String inferenceClassification;
    public EmailMessageBody body;
    public Object sender;
    public Object from;
    public ArrayList<Object> toRecipients;
    public ArrayList<Object> ccRecipients;
    public ArrayList<Object> bccRecipients;
    public ArrayList<Object> replyTo;
    public Object flag;

    /**
     * Get Email Message Attachment using Message I
     * @return EmailMessageAttachment
     */
    public EmailMessageAttachment emailMessageAttachment() {
        RequestEntity requestEntity = RequestEntityUtil.init(EmailEnum.EMAIL_MESSAGE_ATTACHMENTS, EmailMessageAttachments.class)
            .inlineVariables(this.id)
            .headers(new HashMap<String, String>() {
                {
                    put("Authorization", "Bearer " + EmailConnection.getEmailAccessToken());
                }
            }).expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<EmailMessageAttachments> emailMessageAttachmentsResponse = HTTPRequest.build(requestEntity).get();

        return emailMessageAttachmentsResponse.getResponseEntity().value.get(0);
    }

    /**
     * Delete email using email message id
     */
    public void deleteEmailMessage() {
        RequestEntity requestEntity = RequestEntityUtil.init(EmailEnum.EMAIL_MESSAGE, null)
            .inlineVariables(this.id)
            .headers(new HashMap<String, String>() {
                {
                    put("Authorization", "Bearer " + EmailConnection.getEmailAccessToken());
                }
            }).expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        HTTPRequest.build(requestEntity).delete();
    }
}
