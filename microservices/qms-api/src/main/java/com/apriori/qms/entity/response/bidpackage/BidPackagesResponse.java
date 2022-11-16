package com.apriori.qms.entity.response.bidpackage;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "BidPackagesResponseSchema.json")
public class BidPackagesResponse extends Pagination {
    private List<BidPackages> items;
}