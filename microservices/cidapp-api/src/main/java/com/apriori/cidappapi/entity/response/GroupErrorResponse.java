package com.apriori.cidappapi.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "GroupErrorResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupErrorResponse {
    private String path;
    private String method;
    private int status;
    private String error;
    private String message;
}
