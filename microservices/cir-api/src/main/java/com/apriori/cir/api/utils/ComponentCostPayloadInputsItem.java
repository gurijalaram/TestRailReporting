package com.apriori.cir.api.utils;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentCostPayloadInputsItem {
    private int limit;
    private String name;
    private int offset;
    private String value;
}
