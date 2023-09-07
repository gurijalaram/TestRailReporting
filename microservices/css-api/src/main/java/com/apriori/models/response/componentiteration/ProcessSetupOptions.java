package com.apriori.models.response.componentiteration;

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
}
