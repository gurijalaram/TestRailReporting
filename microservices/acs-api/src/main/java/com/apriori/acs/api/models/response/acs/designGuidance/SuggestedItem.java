package com.apriori.acs.api.models.response.acs.designGuidance;

import lombok.Data;

@Data
public class SuggestedItem {
    private Boolean minInclusive;
    private Double maxValue;
    private Boolean maxInclusive;
}