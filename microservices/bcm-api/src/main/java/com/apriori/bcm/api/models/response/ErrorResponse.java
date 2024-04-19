package com.apriori.bcm.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(location = "ErrorResponse.json")
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String method;
}
