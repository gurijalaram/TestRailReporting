package com.apriori.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private Integer pageNumber;
    private Integer pageSize;
}
