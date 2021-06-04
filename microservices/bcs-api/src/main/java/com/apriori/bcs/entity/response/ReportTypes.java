package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "ReportTypesSchema.json")
public class ReportTypes extends Pagination {
    private ReportTypes response;
    private List<ReportType> items;

    public ReportTypes getResponse() {
        return this.response;
    }

    public ReportTypes setResponse(ReportTypes response) {
        this.response = response;
        return this;
    }

    public List<ReportType> getItems() {
        return this.items;
    }

    public ReportTypes setItems(List<ReportType> items) {
        this.items = items;
        return this;
    }
}
