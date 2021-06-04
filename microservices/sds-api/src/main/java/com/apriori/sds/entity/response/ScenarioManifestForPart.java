package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioManifestForPart.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioManifestForPart {
    private String path;
    private String error;
    private String message;
    private String timestamp;
    private String status;
}
