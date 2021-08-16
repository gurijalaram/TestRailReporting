package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "InstallationsSchema.json")
public class InstallationResponse extends Pagination {
    private List<InstallationItems> items;
    private InstallationResponse response;

    public InstallationResponse getResponse() {
        return this.response;
    }

    public InstallationResponse setResponse(InstallationResponse response) {
        this.response = response;
        return this;
    }

    public InstallationResponse setItems(List<InstallationItems> items) {
        this.items = items;
        return this;
    }

    public List<InstallationItems> getItems() {
        return this.items;
    }
}
