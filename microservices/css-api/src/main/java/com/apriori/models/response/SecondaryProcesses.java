package com.apriori.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecondaryProcesses {
    @JsonProperty("Other Secondary Processes")
    private List<String> otherSecondaryProcesses;
    @JsonProperty("Heat Treatment")
    private List<String> heatTreatment;
    @JsonProperty("Machining")
    private List<String> machining;
    @JsonProperty("Surface Treatment")
    private List<String> surfaceTreatment;

}