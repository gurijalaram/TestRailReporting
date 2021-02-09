package com.apriori.apibase.services.cds.objects.AccessControls;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cds/CdsAccessControlsSchema.json")
public class AccessControlResponse extends Pagination {

    private AccessControlResponse response;
    private List<AccessControlItems> items;

    public AccessControlResponse getResponse() {
        return response;
    }

    public AccessControlResponse setResponse(AccessControlResponse response) {
        this.response = response;
        return this;
    }

    public AccessControlResponse setItems(List<AccessControlItems> items) {
        this.items = items;
        return this;
    }

    public List<AccessControlItems> getItems() {
        return this.items;
    }
}
