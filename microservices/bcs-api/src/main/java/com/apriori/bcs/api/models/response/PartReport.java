package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "PartReportSchema.json")
public class PartReport {
    private String contentType;
    private String encodedContent;
}
