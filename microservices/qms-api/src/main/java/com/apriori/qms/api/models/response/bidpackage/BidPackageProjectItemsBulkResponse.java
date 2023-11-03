package com.apriori.qms.api.models.response.bidpackage;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemsBulkResponseSchema.json")
public class BidPackageProjectItemsBulkResponse {
    private List<BidPackageProjectItemResponse> projectItem;
    private List<FailedProjectItem> failedProjectItem;
}
