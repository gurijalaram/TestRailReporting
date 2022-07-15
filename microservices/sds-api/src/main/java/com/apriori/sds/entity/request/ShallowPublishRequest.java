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
public class ShallowPublishRequest {
    private String assignedTo;
    private boolean locked;
    private boolean override;
    private String scenarioName;
    private String status;
    private boolean publishSubComponents;
}
