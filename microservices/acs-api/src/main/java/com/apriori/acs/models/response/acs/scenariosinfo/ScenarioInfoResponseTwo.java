package com.apriori.acs.models.response.acs.scenariosinfo;

import com.apriori.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/ScenariosInfoResponse.json")
public class ScenarioInfoResponseTwo {
    public List<ScenariosInfoResponse> scenarioDetailsList;
}
