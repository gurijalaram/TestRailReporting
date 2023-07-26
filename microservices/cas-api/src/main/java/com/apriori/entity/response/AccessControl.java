package com.apriori.entity.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "AccessControlSchema.json")
@JsonRootName("response")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessControl {
    private String identity;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String customerIdentity;
    private String customerAssociationIdentity;
    private String userIdentity;
    private String applicationIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private Boolean outOfContext;
    private String applicationName;
    private String applicationIconUrl;
    private String customerName;
    private String deploymentName;
    private String installationName;
    private String regionName;
    private String createdByName;
}