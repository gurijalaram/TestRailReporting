package com.apriori.apibase.services.cas.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "cas/NotFoundName.json")
public class NotFoundNameResponse extends Pagination {
    private NotFoundNameResponse response;
    private List<NotFoundName> items;

    public NotFoundNameResponse getResponse() {
        return response;
    }

    public void setResponse(NotFoundNameResponse response) {
        this.response = response;
    }

    public List<NotFoundName> getItems() {
        return this.items;
    }

    public NotFoundNameResponse setItems(List<NotFoundName> items) {
        this.items = items;
        return this;
    }
}
