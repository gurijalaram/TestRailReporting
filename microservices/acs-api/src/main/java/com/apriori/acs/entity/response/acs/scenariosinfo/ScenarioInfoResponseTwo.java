package com.apriori.acs.entity.response.acs.scenariosinfo;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ScenariosInfoResponse.json")
public class ScenarioInfoResponseTwo {
    public List<ScenariosInfoResponse> scenarioDetailsList;
}
