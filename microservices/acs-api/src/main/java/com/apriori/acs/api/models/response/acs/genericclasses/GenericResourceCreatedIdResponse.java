package com.apriori.acs.api.models.response.acs.genericclasses;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericResourceCreatedIdResponse.json")
public class GenericResourceCreatedIdResponse {
    private String id;
    private String resourceCreated;
}
