package com.apriori.qds.entity.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackagesResponseSchema.json")
public class BidPackagesResponse extends Pagination {
    private List<BidPackages> items;
}
