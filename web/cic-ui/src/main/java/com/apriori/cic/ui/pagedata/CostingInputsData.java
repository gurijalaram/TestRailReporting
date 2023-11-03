package com.apriori.cic.ui.pagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostingInputsData {
    private String fieldName;
    private String operatorName;
    private String fieldValue;
}

