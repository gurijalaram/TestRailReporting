package com.apriori.bcs.entity.response;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "CustomAttributesItemsResponseSchema.json")
public class CustomAttributes extends Pagination {
    private String[] allowedValues;
    private Integer ordinal;
    private String identity;
    private String customerIdentity;
    private String displayName;
    private Boolean multiSelect;
    private String name;
    private String requiredAttributeType;
    private String type;
    private CustomAttributes[] items;
    private String defaultValue;
    private Integer precision;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<String> options;
}