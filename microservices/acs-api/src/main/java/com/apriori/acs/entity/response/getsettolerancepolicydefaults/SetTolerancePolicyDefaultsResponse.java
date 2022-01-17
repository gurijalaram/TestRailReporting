package com.apriori.acs.entity.response.getsettolerancepolicydefaults;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

@Data
@Schema(location = "SetTolerancePolicyDefaultsResponse.json")
public class SetTolerancePolicyDefaultsResponse {
    private String resourceCreated;
}
