package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "WorksheetInputRowSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class WorkSheetInputRowResponse {
    String id;
    String identity;
    String customerIdentity;
    String createdAt;
    String createdBy;
    String worksheetId;
    String componentIdentity;
    String scenarioIdentity;
    List<String> iterationIds;
}
