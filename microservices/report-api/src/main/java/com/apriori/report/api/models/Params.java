package com.apriori.report.api.models;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Params {
    private String name;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssZ.class)
    private String value;
    private String type;
}
