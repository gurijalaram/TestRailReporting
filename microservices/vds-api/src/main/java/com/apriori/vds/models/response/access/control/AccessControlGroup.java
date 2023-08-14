package com.apriori.vds.models.response.access.control;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "AccessControlGroup.json")
@Data
@JsonRootName(value = "response")
public class AccessControlGroup {
    private Object attributes;
    private String createdBy;
    private String customerIdentity;
    private String deletedBy;
    private String description;
    private String groupId;
    private String identity;
    private List<String> members;
    private String name;
    private String parentGroupIdentity;
    private List<AccessControlPermission> permissions;
    private Boolean systemGroup;
    private String type;
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
}
