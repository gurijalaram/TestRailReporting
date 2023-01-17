package com.apriori.cisapi.entity.response.bidpackage;


import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemsResponseSchema.json")
public class BidPackageProjectItemsResponse extends Pagination {
    private List<BidPackageProjectItemResponse> items;
}
