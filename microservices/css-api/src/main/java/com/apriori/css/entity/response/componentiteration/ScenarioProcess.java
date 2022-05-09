package com.apriori.css.entity.response.componentiteration;

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
}
