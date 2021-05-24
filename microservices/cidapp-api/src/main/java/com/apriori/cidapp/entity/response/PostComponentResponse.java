package com.apriori.cidapp.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/PostComponentResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComponentResponse {
    private String iterationIdentity;
    private String componentIdentity;
    private String scenarioIdentity;
}

