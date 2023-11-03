package com.apriori.acs.api.models.response.acs.genericclasses;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericResourceCreatedResponse.json")
public class GenericResourceCreatedResponse {
    private String resourceCreated;
}
