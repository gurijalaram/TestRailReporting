package com.apriori.acs.entity.response.acs.genericclasses;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericErrorResponse.json")
public class GenericErrorResponse {
    private Integer errorCode;
    private String errorMessage;
}
