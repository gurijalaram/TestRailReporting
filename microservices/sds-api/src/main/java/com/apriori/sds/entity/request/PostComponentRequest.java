package com.apriori.sds.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComponentRequest {
    private String filename;
    private String scenarioName;
    private CustomAttributesRequest customAttributesRequest;
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
