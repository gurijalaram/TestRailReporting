package com.apriori.bcs.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import lombok.Data;

import java.util.List;

@Schema(location = "ReportTypesItemsResponseSchema.json")
@Data
public class ReportTypes extends Pagination {
    private ReportTypes response;
    private List<ReportType> items;
}
