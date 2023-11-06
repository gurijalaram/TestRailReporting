package com.apriori.shared.util.models.response.component.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScenarioRoutings {
    private String identity;
    private Boolean autoSelected;
    private String digitalFactoryName;
    private String name;
    private Integer piecePartCost;
    private Integer capitalInvestment;
    private Integer fullyBurdenedCost;
    private String processGroupName;
    private String routingState;
    private Boolean userSelected;
}
