package com.apriori.acs.models.response.acs.genericclasses;

import com.apriori.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericResourceCreatedIdResponse.json")
public class GenericResourceCreatedIdResponse {
    private String id;
    private String resourceCreated;
}
