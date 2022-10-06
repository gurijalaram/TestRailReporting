package com.apriori.qds.entity.response.layout;

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
    public String layout;
    public ArrayList<String> visibleColumns;
    public ArrayList<LayoutSortModel> sortModel;
    public String joinType;
    public String viewType;
    public ArrayList<String> pinnedColumns;
    public Integer pageSize;
    public ArrayList<Object> filters;
    public LayoutViewElementConfigList insightFor3D;
    public LayoutViewElementConfigList insightForList;
    public LayoutViewElementConfigList scenarioResult;
}
