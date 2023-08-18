package com.apriori.cidappapi.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.models.response.CostingInput;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(location = "CostingTemplate.json")
@JsonRootName("response")
public class CostingTemplate {
    private String identity = null;
    private String costingTemplateIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime lockedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime publishedAt;
    private String createdBy;
    private String publishedBy;
    private String lockedBy;
    private String deletedBy;
    private String description;
    private String customerIdentity;
    private Boolean locked;
    private Boolean published;
    @Builder.Default
    private Integer annualVolume = 500;
    @Builder.Default
    private Integer batchSize = 458;
    private Integer materialUnitCost;
    private Integer materialUtilization;
    private String customAttributes;
    @Builder.Default
    private String materialMode = "MANUAL";
    private String materialUtilizationMode;
    private String machiningMode;
    private String materialStockName;
    @Builder.Default
    private String materialName = "Steel, Hot Worked, AISI 1010";
    private String name;
    private String processGroupName;
    @Builder.Default
    private Double productionLife = 5.0;
    private Double targetCost;
    private Double targetMass;
    @Builder.Default
    private String vpeName = DigitalFactoryEnum.APRIORI_USA.getDigitalFactory();
    private String twoModelSourceScenarioIdentity;
    @Builder.Default
    private Boolean deleteTemplateAfterUse = false;
    private Boolean usePrimaryDigitalFactoryAsDefaultForSecondaryDigitalFactories;
    @Builder.Default
    private List<String> propertiesToReset = null;
    private List<RoutingNodeOptions> routingNodeOptions;
    private CostingInput.ProcessSetupOptions processSetupOptions;
    private CostingInput.SecondaryDigitalFactories secondaryDigitalFactories;
    private CostingInput.SecondaryProcesses secondaryProcesses;
    private CostingInput.Threads threads;
    private CostingInput.Tolerances tolerances;
}