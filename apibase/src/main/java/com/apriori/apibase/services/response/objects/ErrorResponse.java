package com.apriori.apibase.services.response.objects;

import java.util.ArrayList;

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
