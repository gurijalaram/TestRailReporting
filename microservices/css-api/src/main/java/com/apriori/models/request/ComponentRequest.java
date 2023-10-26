package com.apriori.models.request;

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
}
// TODO: 25/10/2023 marked for deletion
