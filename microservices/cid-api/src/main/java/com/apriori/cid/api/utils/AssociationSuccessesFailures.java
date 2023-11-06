package com.apriori.cid.api.utils;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.request.component.Failures;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "ScenarioSuccessesFailures.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName("response")
public class AssociationSuccessesFailures {
    private List<AssociationStatus> successes;
    private List<Failures> failures;
}
