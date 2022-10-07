package com.apriori.qds.entity.response.bidpackage;


import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageItemsResponseSchema.json")
public class BidPackageItemsResponse {
    private List<BidPackageItemResponse> items;
}
