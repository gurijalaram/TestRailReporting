package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CdsErrorSchema.json")
@Data
public class CdsErrorResponse {
    private String path;
    private String method;
    private Integer status;
    private String error;
    private String message;
}