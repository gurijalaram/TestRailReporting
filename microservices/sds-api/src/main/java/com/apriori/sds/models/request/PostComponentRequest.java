package com.apriori.sds.models.request;

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
    private String scenarioName;
    private String componentName;
    private String componentType;
    private String notes;
    private String description;
    private String updatedBy;
    private String createdBy;
    private Boolean override;
    private String fileMetadataIdentity;
}
