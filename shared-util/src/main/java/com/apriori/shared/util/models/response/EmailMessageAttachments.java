package com.apriori.shared.util.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@Schema(location = "EmailMessageAttachmentsSchema.json")
public class EmailMessageAttachments {
    @JsonProperty("@odata.context")
    public String context;
    public List<EmailMessageAttachment> value;
}