package com.apriori.cmp.entity.response;

import com.apriori.utils.http.enums.Schema;

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