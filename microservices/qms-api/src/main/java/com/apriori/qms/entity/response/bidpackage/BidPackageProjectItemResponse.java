package com.apriori.qms.entity.response.bidpackage;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemResponseSchema.json")
public class BidPackageProjectItemResponse {
    private String identity;
    private String createdAt;
    private String createdBy;
    private String bidPackageIdentity;
    private String projectIdentity;
    private BidPackageItem bidPackageItem;
}
