package com.apriori.acs.api.models.response.acs.artifacttableinfo;

import lombok.Data;

@Data
public class LeadingColumnOrderIndexItem {
    private Integer artifact;
    private Integer properties;
    private Integer technique;
    private Integer results;
}