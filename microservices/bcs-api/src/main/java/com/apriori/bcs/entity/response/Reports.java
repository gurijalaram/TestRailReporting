package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ReportItemsResponseSchema.json")
public class Reports extends Pagination {
    private List<Report> items;
}
