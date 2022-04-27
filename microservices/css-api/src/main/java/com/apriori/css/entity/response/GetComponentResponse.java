package com.apriori.css.entity.response;

import java.util.List;

import com.apriori.apibase.services.common.objects.Pagination;

public class GetComponentResponse extends Pagination {
    private List<GetComponentItems> items;

    public List<GetComponentItems> getItems() {
        return items;
    }
}
