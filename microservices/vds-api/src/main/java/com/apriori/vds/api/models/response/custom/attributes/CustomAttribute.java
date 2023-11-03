package com.apriori.vds.api.models.response.custom.attributes;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "CustomAttribute.json")
@Data
@JsonRootName(value = "response")
public class CustomAttribute {
    private String createdBy;
    private String customerIdentity;
    private String defaultValue;
    private String deletedBy;
    private String displayName;
    private String identity;
    private Boolean multiSelect;
    private String name;
    private Integer ordinal;
    private Integer precision;
    private String requiredAttributeType;
    private String type;
    private String updatedBy;
    private List<String> options;
    private List<String> scope;
    private Boolean searchable;
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
