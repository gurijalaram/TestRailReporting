package com.apriori.qms.entity.response.bidpackage;

import com.apriori.utils.http.enums.Schema;

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
