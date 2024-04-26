package com.apriori.acs.api.models.response.acs.materialsinfo;

import com.apriori.acs.api.models.response.acs.genericclasses.GenericExtendedPropertyInfoItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PropertyInfoMap {
    private GenericExtendedPropertyInfoItem altName2;
    private GenericExtendedPropertyInfoItem altName3;
    private GenericExtendedPropertyInfoItem altName1;
    private GenericExtendedPropertyInfoItem hardnessSystem;
    private GenericExtendedPropertyInfoItem description;
    private GenericExtendedPropertyInfoItem tensileYieldStrength;
    private GenericExtendedPropertyInfoItem scrapCostPercent;
    private GenericExtendedPropertyInfoItem ultimateTensileStrength;
    private GenericExtendedPropertyInfoItem costPerKG;
    private GenericExtendedPropertyInfoItem density;
    @JsonProperty("k")
    private GenericExtendedPropertyInfoItem kkValue;
    private GenericExtendedPropertyInfoItem shearStrength;
    private GenericExtendedPropertyInfoItem youngModulus;
    @JsonProperty("n")
    private GenericExtendedPropertyInfoItem nnValue;
    private GenericExtendedPropertyInfoItem possionRatio;
    private GenericExtendedPropertyInfoItem hardness;
    @JsonProperty("r")
    private GenericExtendedPropertyInfoItem rrValue;
    private GenericExtendedPropertyInfoItem materialCutCodeName;
    private GenericExtendedPropertyInfoItem name;
    private GenericExtendedPropertyInfoItem sourceName;
    private GenericExtendedPropertyInfoItem costUnits;
    private GenericExtendedPropertyInfoItem millingSpeed;
    private GenericExtendedPropertyInfoItem dataSource;
    private GenericExtendedPropertyInfoItem altName4;
    private GenericExtendedPropertyInfoItem costPerUnit;
    private GenericExtendedPropertyInfoItem altName5;
    private GenericExtendedPropertyInfoItem materialTypeName;
    private GenericExtendedPropertyInfoItem baseCostPerUnit;
    private GenericExtendedPropertyInfoItem process;
    private GenericExtendedPropertyInfoItem manufacturer;
    private GenericExtendedPropertyInfoItem tappedDensity;
    private GenericExtendedPropertyInfoItem maxPowderCycles;
    private GenericExtendedPropertyInfoItem recyclePercentage;
    private GenericExtendedPropertyInfoItem heatOfFusion;
    private GenericExtendedPropertyInfoItem solidusTemp;
    private GenericExtendedPropertyInfoItem specificHeatRT;
    private GenericExtendedPropertyInfoItem thermalConductivity;
    private GenericExtendedPropertyInfoItem squareBarCostPerUnit;
    private GenericExtendedPropertyInfoItem rectangularBarCostPerKG;
    private GenericExtendedPropertyInfoItem roundBarCostPerUnit;
    private GenericExtendedPropertyInfoItem angleBarCostPerUnit;
    private GenericExtendedPropertyInfoItem hexBarCostUnits;
    @JsonProperty("tBeamCostUnits")
    private GenericExtendedPropertyInfoItem teeBeamCostUnits;
    private GenericExtendedPropertyInfoItem baseRoundBarCostPerUnit;
    private GenericExtendedPropertyInfoItem baseHexBarCostPerUnit;
    private GenericExtendedPropertyInfoItem rectangularBarCostUnits;
    private GenericExtendedPropertyInfoItem hexBarCostPerUnit;
    private GenericExtendedPropertyInfoItem roundBarCostPerKG;
    private GenericExtendedPropertyInfoItem baseTBeamCostPerUnit;
    private GenericExtendedPropertyInfoItem baseRectangularBarCostPerUnit;
    private GenericExtendedPropertyInfoItem baseSquareBarCostPerUnit;
    @JsonProperty("tBeamCostPerKG")
    private GenericExtendedPropertyInfoItem teeBeamCostPerKG;
    private GenericExtendedPropertyInfoItem roundTubeCostUnits;
    private GenericExtendedPropertyInfoItem baseSheetCostPerUnit;
    private GenericExtendedPropertyInfoItem squareBarCostPerKG;
    private GenericExtendedPropertyInfoItem rectangularTubeCostPerKG;
    private GenericExtendedPropertyInfoItem baseSquareTubeCostPerUnit;
    private GenericExtendedPropertyInfoItem roundTubeCostPerKG;
    private GenericExtendedPropertyInfoItem angleBarCostPerKG;
    private GenericExtendedPropertyInfoItem rectangularTubeCostUnits;
    private GenericExtendedPropertyInfoItem squareBarCostUnits;
    private GenericExtendedPropertyInfoItem sheetCostPerKG;
    private GenericExtendedPropertyInfoItem sheetCostUnits;
    @JsonProperty("iBeamCostPerUnit")
    private GenericExtendedPropertyInfoItem iiBeamCostPerUnit;
    @JsonProperty("iBeamCostUnits")
    private GenericExtendedPropertyInfoItem iiBeamCostUnits;
    private GenericExtendedPropertyInfoItem rectangularTubeCostPerUnit;
    private GenericExtendedPropertyInfoItem roundTubeCostPerUnit;
    private GenericExtendedPropertyInfoItem channelBarCostUnits;
    private GenericExtendedPropertyInfoItem angleBarCostUnits;
    private GenericExtendedPropertyInfoItem baseChannelBarCostPerUnit;
    @JsonProperty("tBeamCostPerUnit")
    private GenericExtendedPropertyInfoItem teeBeamCostPerUnit;
    private GenericExtendedPropertyInfoItem channelBarCostPerKG;
    @JsonProperty("iBeamCostPerKG")
    private GenericExtendedPropertyInfoItem iiBeamCostPerKG;
    private GenericExtendedPropertyInfoItem rectangularBarCostPerUnit;
    private GenericExtendedPropertyInfoItem sheetCostPerUnit;
    private GenericExtendedPropertyInfoItem channelBarCostPerUnit;
    private GenericExtendedPropertyInfoItem roundBarCostUnits;
    private GenericExtendedPropertyInfoItem squareTubeCostPerKG;
    private GenericExtendedPropertyInfoItem baseRoundTubeCostPerUnit;
    private GenericExtendedPropertyInfoItem hexBarCostPerKG;
    private GenericExtendedPropertyInfoItem squareTubeCostUnits;
    private GenericExtendedPropertyInfoItem baseRectangularTubeCostPerUnit;
    private GenericExtendedPropertyInfoItem turningSpeed;
    private GenericExtendedPropertyInfoItem squareTubeCostPerUnit;
    private GenericExtendedPropertyInfoItem baseIBeamCostPerUnit;
    private GenericExtendedPropertyInfoItem baseAngleBarCostPerUnit;
    private GenericExtendedPropertyInfoItem chamberType;
    private GenericExtendedPropertyInfoItem clampingMNPerSqM;
    private GenericExtendedPropertyInfoItem liquidusTemp;
    private GenericExtendedPropertyInfoItem dieLife;
    private GenericExtendedPropertyInfoItem injectTemp;
    private GenericExtendedPropertyInfoItem yieldLossFactor;
    private GenericExtendedPropertyInfoItem moldTemp;
    private GenericExtendedPropertyInfoItem specificHeatSolidus;
    private GenericExtendedPropertyInfoItem coolingFactor;
    private GenericExtendedPropertyInfoItem ejectionTemp;
    private GenericExtendedPropertyInfoItem pourTemp;
    private GenericExtendedPropertyInfoItem annealedTensileYieldStrength;
    private GenericExtendedPropertyInfoItem annealedUltimateTensileStrength;
    private GenericExtendedPropertyInfoItem forgingTemperature;
    private GenericExtendedPropertyInfoItem specificHeatCapacity;
    private GenericExtendedPropertyInfoItem strengthCoefficient;
    private GenericExtendedPropertyInfoItem strainRateSensitivity;
    private GenericExtendedPropertyInfoItem meltingTemp;
    private GenericExtendedPropertyInfoItem ejectDeflectionTemp;
    private GenericExtendedPropertyInfoItem flowLengthRatio;
    private GenericExtendedPropertyInfoItem cureTime;
    private GenericExtendedPropertyInfoItem compressionMoldingPressureMax;
    private GenericExtendedPropertyInfoItem compressionMoldingPressureMin;
    private GenericExtendedPropertyInfoItem canCM;
    private GenericExtendedPropertyInfoItem canRIM;
    @JsonProperty("canIM_SFM")
    private GenericExtendedPropertyInfoItem canIMSFM;
    private GenericExtendedPropertyInfoItem materialForm;
    private GenericExtendedPropertyInfoItem injectionPressureMax;
    private GenericExtendedPropertyInfoItem injectionPressureMin;
    private GenericExtendedPropertyInfoItem canRegrind;
    private GenericExtendedPropertyInfoItem densityMelt;
    private GenericExtendedPropertyInfoItem specificHeat;
    private GenericExtendedPropertyInfoItem graphitePercentContent;
    private GenericExtendedPropertyInfoItem coefC;
    private GenericExtendedPropertyInfoItem coefB;
    private GenericExtendedPropertyInfoItem coefA;
    private GenericExtendedPropertyInfoItem maxGreenDensity;
    private GenericExtendedPropertyInfoItem minGreenDensity;
    private GenericExtendedPropertyInfoItem apparentDensity;
    private GenericExtendedPropertyInfoItem canRotationalMold;
    private GenericExtendedPropertyInfoItem canBlowMold;
    private GenericExtendedPropertyInfoItem basePlateCostPerUnit;
    private GenericExtendedPropertyInfoItem plateCostPerKG;
    private GenericExtendedPropertyInfoItem plateCostUnits;
    private GenericExtendedPropertyInfoItem plateCostPerUnit;
    private GenericExtendedPropertyInfoItem carbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem roundBarCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem squareBarCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem plateCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem roundTubeCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem rectangularBarCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem hexBarCarbonEmissionsFactor;
    @JsonProperty("iBeamCarbonEmissionsFactor")
    private GenericExtendedPropertyInfoItem iiBeamCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem squareTubeCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem rectangularTubeCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem maximumExtrusionExitSpeed;
    private GenericExtendedPropertyInfoItem minimumExtrusionExitSpeed;
    private GenericExtendedPropertyInfoItem angleBarCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem sheetCarbonEmissionsFactor;
    @JsonProperty("tBeamCarbonEmissionsFactor")
    private GenericExtendedPropertyInfoItem teeBeamCarbonEmissionsFactor;
    private GenericExtendedPropertyInfoItem channelBarCarbonEmissionsFactor;
}