package com.apriori.models.response;

import com.apriori.authorization.response.Pagination;

import lombok.Data;

import java.util.List;

@Data
public class GetComponentResponse extends Pagination {
    private List<GetComponentItems> items;
}
