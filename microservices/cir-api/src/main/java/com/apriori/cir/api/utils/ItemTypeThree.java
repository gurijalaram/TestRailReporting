package com.apriori.cir.api.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemTypeThree {
    @SuppressWarnings("checkstyle:MemberName")
    @JsonProperty("y")
    private String yValue;
}
