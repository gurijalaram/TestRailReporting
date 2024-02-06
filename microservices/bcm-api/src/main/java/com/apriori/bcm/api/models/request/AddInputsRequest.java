package com.apriori.bcm.api.models.request;

import com.apriori.bcm.api.models.response.AnalysisInput;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddInputsRequest {
    private List<Inputrow> groupItems;
    private AnalysisInput analysisInput;
}