package com.apriori.cirapi.entity.request;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.json.utils.JsonManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

    public static ReportRequest initFromJsonFile(final String jsonFileNameWithoutType) {
        return (ReportRequest) JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream(jsonFileNameWithoutType + ".json"), ReportRequest.class);
    }

    private String reportUnitUri;
    private Boolean async;
    private Boolean allowInlineScripts;
    private String markupType;
    private Boolean interactive;
    private Boolean freshData;
    private Boolean saveDataSnapshot;
    private String transformerKey;
    private Integer pages;
    private String attachmentsPrefix;
    private String baseUrl;
    private ParametersRequest parameters;
}