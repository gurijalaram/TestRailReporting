package com.apriori.cis.models.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackagesResponseSchema.json")
public class BidPackagesResponse extends Pagination {
    private List<BidPackages> items;
}
