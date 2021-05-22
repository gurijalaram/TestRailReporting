package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "PartReportSchema.json")
public class PartReport {
    private String contentType;
    private String encodedContent;
}