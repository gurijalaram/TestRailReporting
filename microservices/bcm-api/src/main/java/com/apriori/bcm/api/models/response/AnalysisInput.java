package com.apriori.bcm.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.models.response.component.SecondaryDigitalFactories;
import com.apriori.shared.util.models.response.component.SecondaryProcesses;
import com.apriori.shared.util.models.response.component.componentiteration.CustomAttributes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisInput {
    private String identity;
    private String customerIdentity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private CustomAttributes customAttributes;
    private GcdProperties gcdProperties;
    private Integer annualVolume;
    private String processGroupName;
    private Double productionLife;
    private List<String> routingNodeOptions;
    private SecondaryDigitalFactories secondaryDigitalFactories;
    private SecondaryProcesses secondaryProcesses;
    private String digitalFactoryName;

    public static class GcdProperties {
    }
}