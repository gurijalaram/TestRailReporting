package com.apriori.css.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;

import java.util.List;

public class GetComponentResponse extends Pagination {
    private List<GetComponentItems> items;

    public List<GetComponentItems> getItems() {
        return items;
    }
}
