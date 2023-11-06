package com.apriori.cic.ui.pagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishResultsData {
    private List<HashMap<String,String>> writeFieldsData;
    private String reportName;
    private String reportCurrencyCode;
    private String reportCostRounding;
}