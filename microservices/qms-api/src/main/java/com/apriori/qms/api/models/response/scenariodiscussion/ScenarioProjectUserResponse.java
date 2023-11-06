package com.apriori.qms.api.models.response.scenariodiscussion;

import com.apriori.shared.util.annotations.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@Schema(location = "ScenarioProjectUsersResponseSchema.json")
public class ScenarioProjectUserResponse extends ArrayList<ScenarioProjectUserInformation> {

}
