package com.apriori.vds.api.models.response.process.group.materials;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "ProcessGroupMaterialResponse.json")
@Data
@JsonRootName(value = "response")
public class ProcessGroupMaterial {
    private Double angleBarCostPerUnit;
    private Double annealedTensileYieldStrength;
    private Double annealedUltimateTensileStrength;
    private Double apparentDensity;
    private Double arealWeight;
    private Double chamberType;
    private Double channelBarCostPerUnit;
    private Double clampingMNPerSqM;
    private Double coefA;
    private Double coefB;
    private Double coefC;
    private Double convFactor;
    private Double coolingFactor;
    private Double coreCellSize;
    private Double costPerUnit;
    private Double cureDwellTime;
    private Double cureHoldTemperature;
    private Double cureTemperatureRampDownRate;
    private Double cureTemperatureRampUpRate;
    private Double curedThickness;
    private Double deformationAverageAngle;
    private Double deformationLimitAngle;
    private Double deformationWarningAngle;
    private Double density;
    private Double densityMelt;
    private Double dieLife;
    private Double ejectDeflectionTemperature;
    private Double ejectionTemperature;
    private Double forgingTemperature;
    private Double graphitePercentContent;
    private Double hardness;
    private Double heatOfFusion;
    private Double hexBarCostPerUnit;
    @JsonProperty("iBeamCostPerUnit")
    private Double beamCostPerUnitI;
    private Double injectionPressureMax;
    private Double injectionPressureMin;
    private Double injectionTemperature;
    @JsonProperty("k")
    private Double valueK;
    private Double liquidusTemperature;
    private Double lowerFormingTemperature;
    private Double lubricantPercentContent;
    private String materialCutCodeName;
    private Double materialUtilization;
    private Double materialWidth;
    private Double maxDrawRatio;
    private Double maxExtrusionExitSpeed;
    private Double maxGreenDensity;
    private Double maxPowderCycles;
    private Double meltingTemperature;
    private Double millingSpeed;
    private Double minExtrusionExitSpeed;
    private Double minGreenDensity;
    private Double minWallThickness;
    private Double moldTemperature;
    @JsonProperty("n")
    private Double valueN;
    private Double normalFormingTemperature;
    private Double plateCostPerUnit;
    private Double possionRatio;
    private Double pourTemperature;
    @JsonProperty("r")
    private Double valueR;
    private Double rectangularBarCostPerUnit;
    private Double rectangularTubeCostPerUnit;
    private Double recyclePercentage;
    private Double roundBarCostPerUnit;
    private Double roundTubeCostPerUnit;
    private Double scrapCostPercent;
    private Double setTemperature;
    private Double shearStrength;
    private Double sheetCostPerUnit;
    private Double solidusTemperature;
    private Double specificHeat;
    private Double specificHeatCapacity;
    private Double specificHeatRT;
    private Double specificHeatSolidus;
    private Double squareBarCostPerUnit;
    private Double squareTubeCostPerUnit;
    private Double strainRateSensitivity;
    private Double strengthCoefficient;
    @JsonProperty("tBeamCostPerUnit")
    private Double beamCostPerUnitT;
    private Double tappedDensity;
    private Double tensileYieldStrength;
    private Double thermalConductivity;
    private Double thermalDiffusivity;
    private Double turningSpeed;
    private Double ultimateTensileStrength;
    private Double upperFormingTemperature;
    private Double yieldLossFactor;
    private Double youngModulus;
    private String altName1;
    private String altName2;
    private String altName3;
    private String altName4;
    private String altName5;
    private String angleBarCostUnits;
    private String canAFP;
    private String canATL;
    private String canBlowMold;
    private String canDieCast;
    private String canHLU;
    @JsonProperty("canIM_SFM")
    private String canIMSFM;
    private String canPermanentMold;
    private String canRIM;
    private String canRotationalMold;
    private String canSandCast;
    private String channelBarCostUnits;
    private String costUnits;
    private String createdBy;
    private String customerIdentity;
    private String dataSource;
    private String deletedBy;
    private String description;
    private String hardnessSystem;
    private String hexBarCostUnits;
    @JsonProperty("iBeamCostUnits")
    private String beamCostUnitsI;
    private String identity;
    private String manufacturer;
    private String materialForm;
    private String materialTypeName;
    private String morphology;
    private String name;
    private String plateCostUnits;
    private String process;
    private String rectangularBarCostUnits;
    private String rectangularTubeCostUnits;
    private String roundBarCostUnits;
    private String roundTubeCostUnits;
    private String sheetCostUnits;
    private String squareBarCostUnits;
    private String squareTubeCostUnits;
    @JsonProperty("tBeamCostUnits")
    private String beamCostUnitsT;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private SustainabilityInfo sustainabilityInfo;
}
