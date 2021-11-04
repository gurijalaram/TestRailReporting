package com.apriori.acs.entity.response.getscenariosinfo;

import com.apriori.utils.http.enums.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(location = "GetScenariosInfoResponse.json")
public class GetScenarioInfoResponseTwo {
    public List<GetScenariosInfoResponse> scenarioDetailsList;
}
