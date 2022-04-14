package com.apriori.acs.entity.response.acs.getartifacttableinfo;

import lombok.Data;

@Data
public class LeadingColumnOrderIndexItem {
    private Integer artifact;
    private Integer properties;
    private Integer technique;
    private Integer results;
}
