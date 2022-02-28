package com.apriori.acs.entity.response.acs.getscenariosinfo;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GetScenariosInfoResponse.json")
public class GetScenarioInfoResponseTwo {
    public List<GetScenariosInfoResponse> scenarioDetailsList;
}
