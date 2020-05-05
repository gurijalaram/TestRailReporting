package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

@Schema(location = "ErrorRequestResponse.json")
public class ErrorRequestResponse {
    private String errorCode;

    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
