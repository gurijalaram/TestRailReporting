package com.apriori.gcd.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Getter;
import lombok.Setter;

@Schema(location = "ErrorMessageSchema.json")
@Getter
@Setter
public class ErrorResponse {
    private String path;
    private String method;
    private String status;
    private String error;
    private String message;
}
