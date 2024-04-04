package com.apriori.cir.api.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ComponentCostPaginationResponse {
    public String uri;
    public String id;
    public String totalCount;
    public ArrayList<ComponentCostPaginationResponseOptionItem> options;
    public String value;
}
