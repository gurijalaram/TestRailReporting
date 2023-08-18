package com.apriori.cir.models.request;

import com.apriori.http.utils.FileResourceUtil;
import com.apriori.json.JsonManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportExportRequest {

    public static ReportExportRequest initFromJsonFile() {
        return JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("ReportExportRequest.json"), ReportExportRequest.class);
    }

    private String outputFormat;
    private Integer pages;
    private String attachmentsPrefix;
    private Boolean allowInlineScripts;
    private String markupType;
    private String baseUrl;
    private Boolean clearContextCache;
}
