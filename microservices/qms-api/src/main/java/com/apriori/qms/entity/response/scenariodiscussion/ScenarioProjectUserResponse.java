package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.annotations.Schema;

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
