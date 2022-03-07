package com.apriori.bcs.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("response")
@Schema(location = "ReportExportSchema.json")
public class ReportExport { 
    private String reportIdentity;
    private String fileName;
    private String contentType;
    private String encodedContent;
}
