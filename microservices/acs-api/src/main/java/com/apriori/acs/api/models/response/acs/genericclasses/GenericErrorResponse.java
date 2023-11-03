package com.apriori.acs.api.models.response.acs.genericclasses;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericErrorResponse.json")
public class GenericErrorResponse {
    private Integer errorCode;
    private String errorMessage;
}
