package com.apriori.apibase.services.common.objects;

import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "common/RolesSchema.json")
public class Roles extends Pagination {
    private List<Role> items;
    private Roles response;

    public Roles getResponse() {
        return this.response;
    }

    public Roles setResponse(Roles response) {
        this.response = response;
        return this;
    }

    /*public Roles setItems(List<Role> items) {
        this.items = items;
        return this;
    }

    public List<Role> getItems() {
        return this.items;
    }*/
}
