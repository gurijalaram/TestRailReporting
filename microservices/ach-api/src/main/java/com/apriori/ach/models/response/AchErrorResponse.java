package com.apriori.ach.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(location = "AchErrorResponseSchema.json")
public class AchErrorResponse {
    private Integer status;
    private String error;
    private String message;
    private String path;
}