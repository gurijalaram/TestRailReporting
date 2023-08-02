package com.apriori.bcs.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ReportItemsResponseSchema.json")
public class Reports extends Pagination {
    private List<Report> items;
}
