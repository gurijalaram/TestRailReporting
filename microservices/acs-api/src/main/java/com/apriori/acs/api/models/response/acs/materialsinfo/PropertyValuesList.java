package com.apriori.acs.api.models.response.acs.materialsinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PropertyValuesList {
    private String process;
    private String name;
    private String altName2;
    private String altName3;
    private String altName1;
    private String hardnessSystem;
    private String description;
    private String manufacturer;
    private Double baseCostPerUnit;
    private Double tappedDensity;
    private Double maxPowderCycles;
    private Double recyclePercentage;
    private Double tensileYieldStrength;
    private Double scrapCostPercent;
    private Double costPerKG;
    private Double ultimateTensileStrength;
    private Double density;
    @JsonProperty("k")
    private Double kkValue;
    private Double shearStrength;
    private Double youngModulus;
    @JsonProperty("n")
    private Double nnValue;
    private Double hardness;
    private Double possionRatio;
    @JsonProperty("r")
    private Double rrValue;
    private String materialCutCodeName;
    private String costUnits;
    private String sourceName;
    private Double millingSpeed;
    private String dataSource;
    private Double costPerUnit;
    private String altName4;
    private String materialTypeName;
    private String altName5;
    private Double heatOfFusion;
    private Double solidusTemp;
    private Double specificHeatRT;
    private Double thermalConductivity;
    private Double squareBarCostPerUnit;
    private Double rectangularBarCostPerKG;
    private Double roundBarCostPerUnit;
    private Double angleBarCostPerUnit;
    private String hexBarCostUnits;
    @JsonProperty("tBeamCostUnits")
    private String teeBeamCostUnits;
    private Double baseRoundBarCostPerUnit;
    private Double baseHexBarCostPerUnit;
    private String rectangularBarCostUnits;
    private Double hexBarCostPerUnit;
    private Double roundBarCostPerKG;
    private Double baseTBeamCostPerUnit;
    private Double baseRectangularBarCostPerUnit;
    private Double baseSquareBarCostPerUnit;
    @JsonProperty("tBeamCostPerKG")
    private Double teeBeamCostPerKG;
    private String roundTubeCostUnits;
    private Double baseSheetCostPerUnit;
    private Double squareBarCostPerKG;
    private Double rectangularTubeCostPerKG;
    private Double baseSquareTubeCostPerUnit;
    private Double roundTubeCostPerKG;
    private Double angleBarCostPerKG;
    private String rectangularTubeCostUnits;
    private String squareBarCostUnits;
    private Double sheetCostPerKG;
    private String sheetCostUnits;
    @JsonProperty("iBeamCostPerUnit")
    private Double iiBeamCostPerUnit;
    @JsonProperty("iBeamCostUnits")
    private String iiBeamCostUnits;
    private Double rectangularTubeCostPerUnit;
    @JsonProperty("iBeamCarbonEmissionsFactor")
    private Double iiBeamCarbonEmissionsFactor;
    private Double roundTubeCostPerUnit;
    private String channelBarCostUnits;
    private String angleBarCostUnits;
    private Double baseChannelBarCostPerUnit;
    @JsonProperty("tBeamCostPerUnit")
    private Double teeBeamCostPerUnit;
    private Double channelBarCostPerKG;
    @JsonProperty("iBeamCostPerKG")
    private Double iiBeamCostPerKG;
    private Double rectangularBarCostPerUnit;
    private Double sheetCostPerUnit;
    private Double channelBarCostPerUnit;
    private String roundBarCostUnits;
    private String squareTubeCarbonEmissionsFactor;
    private Double squareTubeCostPerKG;
    private Double baseRoundTubeCostPerUnit;
    private Double hexBarCostPerKG;
    private String squareTubeCostUnits;
    private Double baseRectangularTubeCostPerUnit;
    private Double turningSpeed;
    private Double squareTubeCostPerUnit;
    private Double baseIBeamCostPerUnit;
    private Double baseAngleBarCostPerUnit;
    private Integer chamberType;
    private Double clampingMNPerSqM;
    private Double liquidusTemp;
    private Double dieLife;
    private Double injectTemp;
    private Double yieldLossFactor;
    private Double moldTemp;
    private Double specificHeatSolidus;
    private Double coolingFactor;
    private Double ejectionTemp;
    private Double pourTemp;
    private Double annealedTensileYieldStrength;
    private Double annealedUltimateTensileStrength;
    private Double forgingTemperature;
    private Double specificHeatCapacity;
    private Double strengthCoefficient;
    private Double strainRateSensitivity;
    private Double meltingTemp;
    private Double ejectDeflectionTemp;
    private Double flowLengthRatio;
    private Double cureTime;
    private Double compressionMoldingPressureMax;
    private Double compressionMoldingPressureMin;
    private Double injectionPressureMax;
    private Double injectionPressureMin;
    private Double densityMelt;
    private Double specificHeat;
    private Boolean canCM;
    private Boolean canRIM;
    @JsonProperty("canIM_SFM")
    private Boolean canIMSFM;
    private Boolean canRegrind;
    private String materialForm;
    private Double graphitePercentContent;
    private Double coefC;
    private Double coefB;
    private Double coefA;
    private Double maxGreenDensity;
    private Double minGreenDensity;
    private Double apparentDensity;
    private Boolean canRotationalMold;
    private Boolean canBlowMold;
    private Double basePlateCostPerUnit;
    private Double plateCostPerKG;
    private String plateCostUnits;
    private Double plateCostPerUnit;
    private Double carbonEmissionsFactor;
    private Double roundBarCarbonEmissionsFactor;
    private Double squareBarCarbonEmissionsFactor;
    private Double plateCarbonEmissionsFactor;
    private Double roundTubeCarbonEmissionsFactor;
    private Double rectangularBarCarbonEmissionsFactor;
    private Double hexBarCarbonEmissionsFactor;
    private Double rectangularTubeCarbonEmissionsFactor;
    private Double maximumExtrusionExitSpeed;
    private Double minimumExtrusionExitSpeed;
    private Double angleBarCarbonEmissionsFactor;
    private Double sheetCarbonEmissionsFactor;
    @JsonProperty("tBeamCarbonEmissionsFactor")
    private Double teeBeamCarbonEmissionsFactor;
    private Double channelBarCarbonEmissionsFactor;
}
