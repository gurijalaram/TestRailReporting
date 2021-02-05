package com.apriori.cds.entity.response.apVersion;

import com.apriori.apibase.services.Pagination;
import com.apriori.cds.entity.response.apVersion.apVersionItem;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cds/CdsApplicationsSchema.json")
public class apVersionResponse extends Pagination {
    private List<apVersionItem> items;
    private apVersionResponse response;

    public apVersionResponse getResponse() {
        return this.response;
    }

    public apVersionResponse setResponse(apVersionResponse response) {
        this.response = response;
        return this;
    }

    public apVersionResponse setItems(List<apVersionItem> items) {
        this.items = items;
        return this;
    }

    public List<apVersionItem> getItems() {
        return this.items;
    }
}
