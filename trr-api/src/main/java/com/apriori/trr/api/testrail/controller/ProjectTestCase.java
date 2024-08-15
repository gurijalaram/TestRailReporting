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
}
