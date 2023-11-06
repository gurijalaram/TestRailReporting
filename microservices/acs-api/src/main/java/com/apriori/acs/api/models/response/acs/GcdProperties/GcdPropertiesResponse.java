package com.apriori.acs.api.models.response.acs.GcdProperties;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdArtifactsSave.json")
public class GcdPropertiesResponse {

    private Integer scenarioInputSet;
    private List<SuccessFailureList> successes;
    private List<SuccessFailureList> failures;
}
