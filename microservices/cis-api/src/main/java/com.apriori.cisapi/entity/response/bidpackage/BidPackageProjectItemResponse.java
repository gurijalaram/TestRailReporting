package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemResponseSchema.json")
public class BidPackageProjectItemResponse {
    private String identity;
    private BidPackageItem bidPackageItem;
}
