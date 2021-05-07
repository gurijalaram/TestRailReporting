package com.apriori.bcs.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
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

    @Data
    static class Composites {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class MultiSpindleMachining {
        private String processGroupIdentity;
    }

    @Data
    static class CastingDie {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class Machining {
        private String processGroupIdentity;
    }

    @Data
    static class AdditiveManufacturing {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class Forging {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class PowderMetal {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class SheetPlastic {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class HeatTreatment {
        private String processGroupIdentity;
    }

    @Data
    static class SheetMetalHydroforming {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class BarTubeFab {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class Assembly {
        private String processGroupIdentity;
    }

    @Data
    static class InjectionMolding {
        private String processGroupIdentity;
    }

    @Data
    static class CastingInvestment {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class SheetMetalTransferDie {
        private String processGroupIdentity;
    }

    @Data
    static class AssemblyPlasticMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class SheetMetal {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class PrintedCircuitBoard {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class AssemblyMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class CastingSand {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class RapidPrototyping {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class Casting {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class PartAssembly {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class PlasticMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class RotoBlowMolding {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class OtherSecondaryProcesses {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }

    @Data
    static class TwoModelMachining {
        private String processGroupIdentity;
    }

    @Data
    static class SheetMetalStretchForming {
        private String processGroupIdentity;
        private String defaultMaterialName;
        private String defaultMaterialIdentity;
    }
}
