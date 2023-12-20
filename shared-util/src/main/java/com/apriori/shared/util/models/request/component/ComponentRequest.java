package com.apriori.shared.util.models.request.component;

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
public class ComponentRequest {
    private String filename;
    private Boolean override;
    private String resourceName;
    private String scenarioName;
    private String componentIdentity;
    private String scenarioIdentity;
}