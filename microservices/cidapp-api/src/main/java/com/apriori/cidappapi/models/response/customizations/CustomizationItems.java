package com.apriori.cidappapi.models.response.customizations;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CustomizationItems {
    private List<CustomAttributes> customAttributes;
    private String customerIdentity;
    private List<DigitalFactories> digitalFactories;
    private List<ExchangeRates> exchangeRates;
    private String identity;
    private List<ProcessGroups> processGroups;
    private List<SiteVariables> siteVariables;
}
