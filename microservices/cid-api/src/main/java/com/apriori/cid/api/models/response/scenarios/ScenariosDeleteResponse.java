package com.apriori.cid.api.models.response.scenarios;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.request.component.Successes;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "ScenarioDeleteResponse.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName("response")
public class ScenariosDeleteResponse {
    List<Successes> successes;
    List<DeleteFailures> failures;
}
