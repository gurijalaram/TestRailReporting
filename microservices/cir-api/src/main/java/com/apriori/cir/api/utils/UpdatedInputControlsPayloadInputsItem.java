package com.apriori.cir.api.utils;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdatedInputControlsPayloadInputsItem {
    private int limit;
    private String name;
    private int offset;
    private List<String> value;
}
