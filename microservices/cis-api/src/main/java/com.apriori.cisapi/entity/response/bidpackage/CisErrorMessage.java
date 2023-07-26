package com.apriori.cisapi.entity.response.bidpackage;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.ErrorResponse;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Schema(location = "ErrorMessageSchema.json")
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CisErrorMessage {
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX.class)
    private LocalDateTime timestamp;
    private Integer status;
    private String method;
    private String error;
    private ArrayList<ErrorResponse> errors;
    private String message;
    private String path;
}
