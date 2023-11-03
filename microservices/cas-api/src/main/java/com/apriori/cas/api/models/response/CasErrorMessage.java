package com.apriori.cas.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(location = "CasErrorMessageSchema.json")
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CasErrorMessage {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX.class)
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}