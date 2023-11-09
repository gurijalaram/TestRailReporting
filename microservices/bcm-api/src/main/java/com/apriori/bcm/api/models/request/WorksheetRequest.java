package com.apriori.bcm.api.models.request;

import com.apriori.shared.util.annotations.CreatableModel;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "WorksheetRequestSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CreatableModel("worksheet")
@JsonRootName("response")
public class WorksheetRequest {
    Worksheet worksheet;
}
