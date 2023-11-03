package com.apriori.shared.util.models.response.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecondaryProcesses {
    @Builder.Default
    @JsonProperty("Other Secondary Processes")
    private List<String> otherSecondaryProcesses = new ArrayList<>();
    @Builder.Default
    @JsonProperty("Heat Treatment")
    private List<String> heatTreatment = new ArrayList<>();
    @Builder.Default
    @JsonProperty("Machining")
    private List<String> machining = new ArrayList<>();
    @Builder.Default
    @JsonProperty("Surface Treatment")
    private List<String> surfaceTreatment = new ArrayList<>();

}