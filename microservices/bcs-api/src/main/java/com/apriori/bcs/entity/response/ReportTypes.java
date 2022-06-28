package com.apriori.bcs.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Schema(location = "ReportTypesItemsResponseSchema.json")
@Data
public class ReportTypes extends Pagination {
    private ReportTypes response;
    private List<ReportType> items;
}
