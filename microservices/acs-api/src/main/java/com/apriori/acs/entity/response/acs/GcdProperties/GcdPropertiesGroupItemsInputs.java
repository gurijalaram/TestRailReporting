package com.apriori.acs.entity.response.acs.GcdProperties;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GcdPropertiesGroupItemsInputs {
    private String artifactKey;
    private String propertiesToSet;
    private List<PropertiesToReset> propertiesToReset;
}
