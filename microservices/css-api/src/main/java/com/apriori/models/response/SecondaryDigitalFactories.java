package com.apriori.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SecondaryDigitalFactories {
    @JsonProperty("Heat Treatment")
    private String heatTreatment;
    @JsonProperty("Machining")
    private String machining;
    @JsonProperty("Surface Treatment")
    private String surfaceTreatment;
    @JsonProperty("Other Secondary Processes")
    private String otherSecondaryProcesses;
}
