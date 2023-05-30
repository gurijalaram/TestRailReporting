package com.apriori.cidappapi.entity.response.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Material {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private Double annealedTensileYieldStrength;
    private Double annealedUltimateTensileStrength;
    private Double apparentDensity;
    private String altName1;
    private String altName2;
    private String altName3;
    private String altName4;
    private String altName5;
    private Boolean canBlowMold;
    private Boolean canDieCast;
    private Boolean canIM_SFM;
    private Boolean canPermanentMold;
    private Boolean canRotationalMold;
    private Boolean canRIM;
    private Boolean canSandCast;
    private Integer chamberType;
    private String chamberTypeName;
    private Double clampingMNPerSqM;
    private Double coefA;
    private Double coefB;
    private Double coefC;
    private Double convFactor;
    private Double coolingFactor;
    private Double costPerUnit;
    private String costUnits;
    private String dataSource;
    private Double density;
    private Double densityMelt;
    private String description;
    private Double dieLife;
    private Double ejectDeflectionTemperature;
    private Double ejectionTemperature;
    private Double forgingTemperature;
    private Double graphitePercentContent;
    private Double hardness;
    private String hardnessSystem;
    private Double injectionPressureMax;
    private Double injectionPressureMin;
    private Double heatOfFusion;
    private Double injectionTemperature;
    private Double liquidusTemperature;
    private Double k;
    private Double lowerFormingTemperature;
    private String manufacturer;
    private String materialCutCodeName;
    private MaterialSustainabilityInfo sustainabilityInfo;
    private String materialForm;
    private String materialTypeName;
    private String meltingTemperature;
    private Double maxDrawRatio;
    private Double maxGreenDensity;
    private Double maxPowderCycles;
    private Double millingSpeed;
    private Double minGreenDensity;
    private Double minWallThickness;
    private String morphology;
    private Double n;
    private String name;
    private Double specificHeat;
    private Double thermalConductivity;
    private String process;
    private Double recyclePercentage;
    private Double tappedDensity;
    private Double possionRatio;
    private Double r;
    private Double setTemperature;
    private Double scrapCostPercent;
    private Double shearStrength;
    private Double specificHeatCapacity;
    private Double solidusTemperature;
    private Double specificHeatRT;
    private Double specificHeatSolidus;
    private Double tensileYieldStrength;
    private Double thermalDiffusivity;
    private Double ultimateTensileStrength;
    private Double youngModulus;
    private Double hexBarCostPerUnit;
    private String hexBarCostUnits;
    private Double plateCostPerUnit;
    private String plateCostUnits;
    private String rectangularBarCostUnits;
    private Double rectangularBarCostPerUnit;
    private Double roundBarCostPerUnit;
    private String roundBarCostUnits;
    private Double roundTubeCostPerUnit;
    private String roundTubeCostUnits;
    private Double squareBarCostPerUnit;
    private String squareBarCostUnits;
    private Double strainRateSensitivity;
    private Double strengthCoefficient;
    private Double turningSpeed;
    private Double upperFormingTemperature;
    private Double yieldLossFactor;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
}
