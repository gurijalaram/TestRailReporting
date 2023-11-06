package com.apriori.bcs.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UserDefinedAttributesSchema.json")
public class UserDefinedAttributes extends Pagination {
    private String identity;
    private String customerIdentity;
    private Integer ordinal;
    private String name;
    private String displayName;
    private String type;
    private String requiredAttributeType;
    private String multiSelect;
    @JsonProperty("ProjectName")
    private String projectName;
    @JsonProperty("ProjectOwner")
    private String projectOwner;
    private String[] allowedValues;
    private List<UserDefinedAttributes> items;
    private String defaultValue;
    private Integer precision;
    private String[] options;
    private String createdBy;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty("UDA5")
    public Object uda5;
    @JsonProperty("UDA4")
    public Object uda4;
    @JsonProperty("UDA3")
    public String uda3;
    @JsonProperty("UDA1")
    public String uda1;
    @JsonProperty("UDA2")
    public Object uda2;
}
