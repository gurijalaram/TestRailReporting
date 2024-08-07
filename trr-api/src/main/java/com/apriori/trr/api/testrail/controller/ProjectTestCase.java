package com.apriori.trr.api.testrail.controller;

import lombok.Builder;
import lombok.Data;

@Data
public class ProjectTestCase {
    private Integer projectID;
    private String projectName;
    private Integer totalCases;
    private Integer automatable;
    private Integer automated;
    private Integer percentageCovered;

    public Integer calculatePercentage() {
       this.totalCases = this.automatable + this.automated;
       if(this.automated ==0 && this.automatable == 0) {
           this.percentageCovered = 0;
       } else {
           this.percentageCovered = Math.round((this.automated * 100) / totalCases);
       }
        return this.percentageCovered;
    }
}
