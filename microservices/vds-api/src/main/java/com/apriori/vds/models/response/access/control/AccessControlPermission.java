package com.apriori.vds.models.response.access.control;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(location = "AccessControlPermission.json")
@Data
@JsonRootName(value = "response")
public class AccessControlPermission {
    private String actions;
    private String createdBy;
    private String cslRule;
    private String customerIdentity;
    private String deletedBy;
    private String description;
    private String expression;
    private String identity;
    private String name;
    private String normalDeny;
    private String normalGrant;
    private String permissionId;
    private String resourceType;
    private String ruleType;
    private String updatedBy;
    private Boolean requiresTransliteration;
    private Boolean isSimpleRule;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
