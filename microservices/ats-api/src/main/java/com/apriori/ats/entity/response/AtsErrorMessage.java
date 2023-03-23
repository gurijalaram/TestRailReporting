package com.apriori.ats.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Schema(location = "AtsErrorMessageSchema.json")
@Data
public class AtsErrorMessage {
    private String path;
    private String method;
    private Integer status;
    private String error;
    private String message;
}