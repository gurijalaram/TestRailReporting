package com.apriori.models.response;

import lombok.Data;

import java.util.List;

@Data
public class GetComponentResponse extends Pagination {
    private List<GetComponentItems> items;
}
