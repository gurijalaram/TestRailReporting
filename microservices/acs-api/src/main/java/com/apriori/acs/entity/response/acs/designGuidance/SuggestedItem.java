package com.apriori.acs.entity.response.acs.designGuidance;

import lombok.Data;

@Data
public class SuggestedItem {
    private Boolean minInclusive;
    private Double maxValue;
    private Boolean maxInclusive;
}