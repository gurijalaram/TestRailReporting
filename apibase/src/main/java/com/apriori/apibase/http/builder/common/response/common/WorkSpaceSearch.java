package com.apriori.apibase.http.builder.common.response.common;

import java.util.List;

public class WorkSpaceSearch {

    private WorkSpaceSearchCriteria criteria;

    private List<String> visibility;

    public WorkSpaceSearchCriteria getCriteria() {
        return criteria;
    }

    public WorkSpaceSearch setCriteria(WorkSpaceSearchCriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public List<String> getVisibility() {
        return visibility;
    }

    public WorkSpaceSearch setVisibility(List<String> visibility) {
        this.visibility = visibility;
        return this;
    }
}
