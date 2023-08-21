package com.apriori.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeatTreatment {
    @JsonProperty("Carburize")
    private String carburize;
    @JsonProperty("Carbonitride")
    private String carbonitride;
    @JsonProperty("Induction Harden")
    private String inductionHarden;
    @JsonProperty("Atmosphere Oil Harden")
    private String atmosphereOilHarden;
    @JsonProperty("Vacuum Air Harden")
    private String vacuumAirHarden;
    @JsonProperty("Vacuum Air Harden with High Temper")
    private String vacuumAirHardenHighTemper;
    @JsonProperty("Spring Steel Harden")
    private String springSteelHarden;
    @JsonProperty("Stainless Steel Harden")
    private String stainlessSteelHarden;
    @JsonProperty("High Speed Steel Harden")
    private String highSpeedSteelHarden;
    @JsonProperty("Standard Anneal")
    private String standardAnneal;
    @JsonProperty("Low Temp Vacuum Anneal")
    private String lowTempVacuumAnneal;
    @JsonProperty("High Temp Vacuum Anneal")
    private String highTempVacuumAnneal;
    @JsonProperty("Standard Temper")
    private String standardTemper;
    @JsonProperty("Vacuum Temper")
    private String vacuumTemper;
    @JsonProperty("Hot Isostatic Pressing")
    private String hotIsostaticPressing;
    @JsonProperty("Solution")
    private String solution;
    @JsonProperty("Age")
    private String age;
    @JsonProperty("Stress Relief")
    private String stressRelief;
    @JsonProperty("Cryogenic Freeze")
    private String cryogenicFreeze;
}
