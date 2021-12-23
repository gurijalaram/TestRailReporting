package com.apriori.cidappapi.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioProcess {
    private String identity;
    private Double capitalInvestment;
    private Boolean costingFailed;
    private Double cycleTime;
    private String displayName;
    private Double fullyBurdenedCost;
    private String machineName;
    private Integer order;
    private String processGroupName;
    private String processName;
    private Double totalCost;
    private String vpeName;
    private List<Gcds> gcds;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Gcds {
        private String artifactTypeName;
        private Integer sequenceNumber;
        private String displayName;
    }
}
