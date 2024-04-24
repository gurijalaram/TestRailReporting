package com.apriori.cir.api.utils;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdatedInputControlsPayloadInputsItem {
    private String criteria;
    private Integer limit;
    private String name;
    private Integer offset;
    private List<String> value;
}
