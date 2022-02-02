package com.apriori.acs.entity.response.getpartprimaryprocessgroups;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "GetPartPrimaryProcessGroupsResponse.json")
public class GetPartPrimaryProcessGroupsResponse {
    private List<String> processGroups;
}
