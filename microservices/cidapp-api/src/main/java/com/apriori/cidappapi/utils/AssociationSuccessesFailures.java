package com.apriori.cidappapi.utils;

import com.apriori.cidappapi.entity.response.Failures;
import com.apriori.utils.http.enums.Schema;

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
