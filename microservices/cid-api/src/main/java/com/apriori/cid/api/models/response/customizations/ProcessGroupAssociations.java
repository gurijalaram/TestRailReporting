package com.apriori.cid.api.models.response.customizations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProcessGroupAssociations {
    @JsonProperty("Composites")
    private Composites composites;
    @JsonProperty("Multi-Spindle Machining")
    private MultiSpindleMachining multiSpindleMachining;
    @JsonProperty("Machining")
    private Machining machining;
    @JsonProperty("Casting - Die")
    private CastingDie castingDie;
    @JsonProperty("Additive Manufacturing")
    private AdditiveManufacturing additiveManufacturing;
    @JsonProperty("Powder Metal")
    private PowderMetal powderMetal;
    @JsonProperty("Forging")
    private Forging forging;
    @JsonProperty("Sheet Plastic")
    private SheetPlastic sheetPlastic;
    @JsonProperty("Heat Treatment")
    private HeatTreatment heatTreatment;
    @JsonProperty("User Guided")
    private UserGuided userGuided;
    @JsonProperty("Sheet Metal - Hydroforming")
    private SheetMetalHydroforming sheetMetalHydroforming;
    @JsonProperty("Bar & Tube Fab")
    private BarTubeFab barTubeFab;
    @JsonProperty("Assembly")
    private Assembly assembly;
    @JsonProperty("Casting - Investment")
    private CastingInvestment castingInvestment;
    @JsonProperty("Sheet Metal - Transfer Die")
    private SheetMetalTransferDie sheetMetalTransferDie;
    @JsonProperty("Assembly Plastic Molding")
    private AssemblyPlasticMolding assemblyPlasticMolding;
    @JsonProperty("Sheet Metal")
    private SheetMetal sheetMetal;
    @JsonProperty("PCB")
    private Pcb pcb;
    @JsonProperty("Casting - Sand")
    private CastingSand castingSand;
    @JsonProperty("Assembly Molding")
    private AssemblyMolding assemblyMolding;
    @JsonProperty("Rapid Prototyping")
    private RapidPrototyping rapidPrototyping;
    @JsonProperty("Casting")
    private Casting casting;
    @JsonProperty("Part Assembly")
    private PartAssembly partAssembly;
    @JsonProperty("Plastic Molding")
    private PlasticMolding plasticMolding;
    @JsonProperty("Stock Machining")
    private StockMachining stockMachining;
    @JsonProperty("Surface Treatment")
    private SurfaceTreatment surfaceTreatment;
    @JsonProperty("Roto & Blow Molding")
    private RotoBlowMolding rotoBlowMolding;
    @JsonProperty("Other Secondary Processes")
    private OtherSecondaryProcesses otherSecondaryProcesses;
    @JsonProperty("2-Model Machining")
    private TwoModelMachining twoModelMachining;
    @JsonProperty("Sheet Metal - Stretch Forming")
    private SheetMetalStretchForming sheetMetalStretchForming;
    @JsonProperty("Sheet Metal - Roll Forming")
    private SheetMetalRollForming sheetMetalRollForming;
}
