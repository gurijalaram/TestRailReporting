package com.apriori.ach.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ApplicationMetadataSchema.json")
@Data
@JsonRootName("response")
public class ApplicationMetadata {
    private String deploymentName;
    private String deploymentType;
    private String installationName;
    private String applicationIdentity;
    private String applicationName;
    private CustomerMetadata customerMetadata;
}