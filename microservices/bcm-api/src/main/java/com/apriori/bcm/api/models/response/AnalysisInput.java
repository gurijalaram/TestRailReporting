package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.models.response.component.SecondaryDigitalFactories;
import com.apriori.shared.util.models.response.component.SecondaryProcesses;
import com.apriori.shared.util.models.response.component.componentiteration.CustomAttributes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisInput {
    private String identity;
    private String customerIdentity;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private CustomAttributes customAttributes;
    private GcdProperties gcdProperties;
    private Integer annualVolume;
    private String processGroupName;
    private Double productionLife;
    private List<String> routingNodeOptions;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private SecondaryProcesses secondaryProcesses;
    private String digitalFactoryName;

    public static class GcdProperties {
    }
}