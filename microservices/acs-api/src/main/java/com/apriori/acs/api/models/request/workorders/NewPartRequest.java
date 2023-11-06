package com.apriori.acs.api.models.request.workorders;

import com.google.common.primitives.Bytes;
import lombok.Data;

@Data
public class NewPartRequest {
    private String filename;
    private Bytes data;
    private String externalId;
    private Integer annualVolume;
    private Integer batchSize;
    private String description;
    private String materialName;
    private String compType;
    private String pinnedRouting;
    private String processGroup;
    private Double productionLife;
    private String scenarioName;
    private String udas;
    private String vpeName;
    private String availablePg;
    private String currencyCode;
    private String materialMode;
    private String machiningMode;
}
