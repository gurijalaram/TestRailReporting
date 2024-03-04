package com.apriori.cic.api.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorJobPart {
    private String annualManufacturingCarbon;
    private String cidPartLink;
    private String costingResult;
    private String customerUuid;
    private String errorMessage;
    private String cycleTime;
    private String dfmRisk;
    private String dfmScore;
    private String finishMass;
    private String fullyBurdenedCost;
    private String jobUuid;
    private String laborTime;
    private String logisticsCarbon;
    private String material;
    private String materialCarbon;
    private String materialCost;
    private String partId;
    private String partIdentity;
    private String partNumber;
    private String partType;
    private String piecePartCost;
    private String processCarbon;
    private String processGroup;
    private String processRouting;
    private String revisionNumber;
    private String roughMass;
    private String scenarioName;
    private String status;
    private String totalCarbon;
    private String utilization;
    private String uuid;
    private String vpe;
}
