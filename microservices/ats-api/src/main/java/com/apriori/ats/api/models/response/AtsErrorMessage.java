package com.apriori.ats.api.models.response;

import com.apriori.shared.util.annotations.Schema;

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