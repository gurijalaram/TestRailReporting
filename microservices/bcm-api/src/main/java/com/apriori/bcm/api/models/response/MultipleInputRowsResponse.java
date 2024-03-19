package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "InputRowGroupsResponseSchema.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class MultipleInputRowsResponse {
    private List<WorkSheetInputRowResponse> successes;
    private List<FailureAnalysisInputs> failures;
}