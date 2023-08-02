package com.apriori.bcs.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import lombok.Data;

import java.util.List;

@Schema(location = "ReportTypesItemsResponseSchema.json")
@Data
public class ReportTypes extends Pagination {
    private ReportTypes response;
    private List<ReportType> items;
}
