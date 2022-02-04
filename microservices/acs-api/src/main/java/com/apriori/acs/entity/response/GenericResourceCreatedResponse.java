package com.apriori.acs.entity.response;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "GenericResourceCreatedResponse.json")
public class GenericResourceCreatedResponse {
    private String resourceCreated;
}
