package com.apriori.cid.api.models.response.scenarios;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "ScenarioAssociations.json")
@Data
@JsonRootName("response")
public class ScenarioAssociations {
    List<AssociationSuccesses> successes;
    List<AssociationFailures> failures;
}
