package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "ScenarioManifestForPart.json")
@Data
@JsonRootName("response")
public class ScenarioManifestForPart {
    private String path;
    private String error;
    private String message;
    private String timestamp;
    private String status;
}
