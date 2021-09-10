package com.apriori.vds.entity.request.process.group.associations;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

// Serialize
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "processGroupAssociation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessGroupAssociationRequest {

    private String createdBy;
    private String defaultVpeIdentity;
    private String defaultVpeName;
    private String deletedBy;
    private String identity;
    private String processGroupIdentity;
    private String processGroupName;
    private String updatedBy;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
}
