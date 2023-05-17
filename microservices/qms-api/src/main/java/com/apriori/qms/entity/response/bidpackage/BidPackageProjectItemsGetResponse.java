package com.apriori.qms.entity.response.bidpackage;


import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackageProjectItemsGetResponseSchema.json")
public class BidPackageProjectItemsGetResponse extends Pagination {
    List<BidPackageProjectItemGetResponse> items;
}
