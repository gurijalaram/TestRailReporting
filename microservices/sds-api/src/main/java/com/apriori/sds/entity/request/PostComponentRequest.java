package com.apriori.sds.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("component")
public class PostComponentRequest {
    private String filename;
    private String scenarioName;
    private String name;
    private Boolean override;
    private String fileContents;
    private String componentType;
    private String componentName;
    private String updatedBy;
    private String createdBy;
    private String description;
    private String notes;
}
