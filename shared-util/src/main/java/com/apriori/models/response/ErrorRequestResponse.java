package com.apriori.models.response;

import com.apriori.annotations.Schema;

import lombok.Getter;
import lombok.Setter;

@Schema(location = "ErrorMessageSchema.json")
@Getter
@Setter
public class ErrorRequestResponse {
    private String path;
    private String method;
    private String status;
    private String error;
    private String message;

}
