package com.apriori.acs.api.models.response.acs.GcdProperties;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GcdPropertiesGroupItemsInputs {
    private String artifactKey;
    private PropertiesToSet propertiesToSet;
    private List<String> propertiesToReset;
}
