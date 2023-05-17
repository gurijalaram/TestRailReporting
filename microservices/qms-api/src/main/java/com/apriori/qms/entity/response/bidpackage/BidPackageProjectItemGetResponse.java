package com.apriori.qms.entity.response.bidpackage;


import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemGetResponseSchema.json")
public class BidPackageProjectItemGetResponse {
    private String identity;
    private String createdAt;
    private String createdBy;
    private String bidPackageIdentity;
    private String projectIdentity;
    private BidPackageItem bidPackageItem;
}
