package com.apriori.qds.models.response.layout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayoutViewElementConfig {
    private String layout;
    private ArrayList<String> visibleColumns;
    private ArrayList<LayoutSortModel> sortModel;
    private String joinType;
    private String viewType;
    private ArrayList<String> pinnedColumns;
    private Integer pageSize;
    private ArrayList<Object> filters;
    private LayoutViewElementConfigList insightFor3D;
    private LayoutViewElementConfigList insightForList;
    private LayoutViewElementConfigList scenarioResult;
}
