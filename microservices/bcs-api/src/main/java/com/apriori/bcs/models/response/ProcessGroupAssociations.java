package com.apriori.bcs.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessGroupAssociations {
    @JsonProperty("Composites")
    private Composites composites;
    @JsonProperty("Multi-Spindle Machining")
    private MultiSpindleMachining multiSpindleMachining;
    @JsonProperty("Casting - Die")
    private CastingDie castingDie;
    @JsonProperty("Machining")
    private Machining machining;
    @JsonProperty("Additive Manufacturing")
    private AdditiveManufacturing additiveManufacturing;
    @JsonProperty("Forging")
    private Forging forging;
    @JsonProperty("Powder Metal")
    private PowderMetal powderMetal;
    @JsonProperty("Sheet Plastic")
    private SheetPlastic sheetPlastic;
    @JsonProperty("Heat Treatment")
    private HeatTreatment heatTreatment;
    @JsonProperty("Sheet Metal - Hydroforming")
    private SheetMetalHydroforming sheetMetalHydroforming;
    @JsonProperty("Bar & Tube Fab")
    private BarTubeFab barTubeFab;
    @JsonProperty("Assembly")
    private Assembly assembly;
    @JsonProperty("Injection Molding")
    private InjectionMolding injectionMolding;
    @JsonProperty("Casting - Investment")
    private CastingInvestment castingInvestment;
    @JsonProperty("Sheet Metal - Transfer Die")
    private SheetMetalTransferDie sheetMetalTransferDie;
    @JsonProperty("Assembly Plastic Molding")
    private AssemblyPlasticMolding assemblyPlasticMolding;
    @JsonProperty("Sheet Metal")
    private SheetMetal sheetMetal;
    @JsonProperty("PCB")
    private PrintedCircuitBoard printedCircuitBoard;
    @JsonProperty("Assembly Molding")
    private AssemblyMolding assemblyMolding;
    @JsonProperty("Casting - Sand")
    private CastingSand castingSand;
    @JsonProperty("Rapid Prototyping")
    private RapidPrototyping rapidPrototyping;
    @JsonProperty("Casting")
    private Casting casting;
    @JsonProperty("Part Assembly")
    private PartAssembly partAssembly;
    @JsonProperty("Plastic Molding")
    private PlasticMolding plasticMolding;
    @JsonProperty("Roto & Blow Molding")
    private RotoBlowMolding rotoBlowMolding;
    @JsonProperty("Other Secondary Processes")
    private OtherSecondaryProcesses otherSecondaryProcesses;
    @JsonProperty("2-Model Machining")
    private TwoModelMachining twoModelMachining;
    @JsonProperty("Sheet Metal - Stretch Forming")
    private SheetMetalStretchForming sheetMetalStretchForming;
    @JsonProperty("User Guided")
    private UserGuided userGuided;
    @JsonProperty("Stock Machining")
    private StockMachining stockMachining;
    @JsonProperty("Surface Treatment")
    private StockMachining surfaceTreatment;

    @Data
    public static class SurfaceTreatment {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class StockMachining {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class Composites {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class MultiSpindleMachining {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class CastingDie {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class Machining {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class AdditiveManufacturing {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class Forging {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class PowderMetal {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class SheetPlastic {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class HeatTreatment {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class SheetMetalHydroforming {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class BarTubeFab {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class Assembly {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class InjectionMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class CastingInvestment {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class SheetMetalTransferDie {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class AssemblyPlasticMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class SheetMetal {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class PrintedCircuitBoard {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class AssemblyMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class CastingSand {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class RapidPrototyping {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class Casting {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class PartAssembly {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class PlasticMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class RotoBlowMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class OtherSecondaryProcesses {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class TwoModelMachining {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    public static class SheetMetalStretchForming {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class UserGuided {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }
}
