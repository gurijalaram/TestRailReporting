package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "CostingTemplateResponse.json")
@Data
@JsonRootName("response")
public class CostingTemplate {
    private String identity;
    private String createdBy;
    private String customerIdentity;
    private Boolean locked;
    private Boolean published;
    private Integer annualVolume;
    private Integer batchSize;
    private List<String> propertiesToReset;
    private Object customAttributes;
    private String materialMode;
    private String materialName;
    private String pinnedRouting;
    private String processGroupName;
    private Object processSetupOptions;
    private ScenarioSecondaryProcess secondaryProcess;
    private Object secondaryVpes;
    private Object secondaryProcesses;
    private Object secondaryDigitalFactories;
    private Object threads;
    private Object tolerances;
    private Integer productionLife;
    private Object vpeName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private List<RoutingNodeOptions> routingNodeOptions;
}
