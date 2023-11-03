package com.apriori.cmp.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(location = "ErrorResponse.json")
public class ErrorResponse {
    private String path;
    private String method;
    private int status;
    private String error;
    private String message;
}