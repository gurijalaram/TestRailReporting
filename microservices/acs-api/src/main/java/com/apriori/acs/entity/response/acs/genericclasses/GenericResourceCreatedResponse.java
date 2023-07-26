package com.apriori.acs.entity.response.acs.genericclasses;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericResourceCreatedResponse.json")
public class GenericResourceCreatedResponse {
    private String resourceCreated;
}
