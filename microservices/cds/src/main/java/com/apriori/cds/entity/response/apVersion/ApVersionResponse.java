package com.apriori.cds.entity.response.apVersion;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cds/CdsApplicationsSchema.json")
public class ApVersionResponse extends Pagination {
    private List<ApVersionItem> items;
    private ApVersionResponse response;

    public ApVersionResponse getResponse() {
        return this.response;
    }

    public ApVersionResponse setResponse(ApVersionResponse response) {
        this.response = response;
        return this;
    }

    public ApVersionResponse setItems(List<ApVersionItem> items) {
        this.items = items;
        return this;
    }

    public List<ApVersionItem> getItems() {
        return this.items;
    }
}
