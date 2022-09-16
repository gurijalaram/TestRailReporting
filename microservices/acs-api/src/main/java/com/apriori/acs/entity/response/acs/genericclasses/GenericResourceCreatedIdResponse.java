package com.apriori.acs.entity.response.acs.genericclasses;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericResourceCreatedIdResponse.json")
public class GenericResourceCreatedIdResponse {
    private String id;
    private String resourceCreated;
}
