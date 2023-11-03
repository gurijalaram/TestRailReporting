package com.apriori.sds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "PostComponentResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComponentResponse {
    private String iterationIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
}
