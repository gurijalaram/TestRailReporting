package com.apriori.acs.entity.response.acs;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "acs/GenericResourceCreatedResponse.json")
public class GenericResourceCreatedResponse {
    private String resourceCreated;
}
