package com.apriori.entity.request;

import com.apriori.utils.http.enums.Schema;

import lombok.Getter;
import lombok.Setter;

@Schema(location = "ErrorRequestResponse.json")
@Getter
@Setter
public class ErrorRequestResponse {
    private String path;
    private String method;
    private String status;
    private String error;
    private String message;

}
