package com.apriori.css.entity.response;

import com.apriori.utils.Pagination;

import lombok.Data;

import java.util.List;

@Data
public class GetComponentResponse extends Pagination {
    private List<GetComponentItems> items;
}
