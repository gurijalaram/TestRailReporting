package com.apriori.authorization.response;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ErrorResponse {
    public ArrayList<String> codes;
    public ArrayList<ErrorArgumentResponse> arguments;
    public String defaultMessage;
    public String objectName;
    public String field;
    public String rejectedValue;
    public boolean bindingFailure;
    public String code;
}