package com.apriori.vds.entity.response.custom.attributes;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Schema(location = "vds/CustomAttribute.json")
@Data
@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomAttribute {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String deletedAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private String updatedAt;
}
