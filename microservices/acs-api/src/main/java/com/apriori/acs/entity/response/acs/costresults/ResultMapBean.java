package com.apriori.acs.entity.response.acs.costresults;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultMapBean {
    @JsonProperty("@type")
    private String propertyValueMap;
    private String propertyInfoMap;
}
