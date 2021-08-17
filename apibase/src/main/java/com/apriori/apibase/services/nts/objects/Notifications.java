package com.apriori.apibase.services.nts.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "CisReportsSchema.json")
public class Notifications extends Pagination {
    private Notifications response;
    private List<Notification> items;

    public Notifications getResponse() {
        return this.response;
    }

    public Notifications setResponse(Notifications response) {
        this.response = response;
        return this;
    }

    public List<Notification> getItems() {
        return this.items;
    }

    public Notifications setItems(List<Notification> items) {
        this.items = items;
        return this;
    }
}
