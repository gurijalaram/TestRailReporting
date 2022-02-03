package com.apriori.acs.entity.response.getpartprimaryprocessgroups;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "GetPartPrimaryProcessGroupsResponse.json")
@AllArgsConstructor
@NoArgsConstructor
public class GetPartPrimaryProcessGroupsResponse extends ArrayList {
    private List<String> processGroups;
}
