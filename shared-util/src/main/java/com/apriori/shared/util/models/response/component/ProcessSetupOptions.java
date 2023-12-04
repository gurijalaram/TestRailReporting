package com.apriori.shared.util.models.response.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessSetupOptions {
    @JsonProperty("Casting - Die")
    public Object castingDie;
    @JsonProperty("Sheet Metal")
    public Object sheetMetal;
    @JsonProperty("Plastic Molding")
    public Object plasticMolding;
    @JsonProperty("Heat Treatment")
    public Object heatTreatment;
    @JsonProperty("Surface Treatment")
    public Object surfaceTreatment;
    @JsonProperty("Powder Metal")
    public Object powderMetal;
}
