package com.apriori.sds.entity.response;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "CostingTemplateResponse.json")
@Data
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostingTemplate {
    private String identity;
    private String createdBy;
    private String customerIdentity;
    private Boolean locked;
    private Boolean published;
    private Object customAttributes;
    private String materialName;
    private String pinnedRouting;
    private String processGroupName;
    private Object processSetupOptions;
    private ScenarioSecondaryProcess secondaryProcess;
    private Object secondaryVpes;
    private Object threads;
    private Object tolerances;
    private Object vpeName;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
}
