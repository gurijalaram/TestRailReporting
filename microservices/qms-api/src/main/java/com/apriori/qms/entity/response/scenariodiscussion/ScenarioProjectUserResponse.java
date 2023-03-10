package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(location = "ScenarioProjectUsersResponseSchema.json")
public class ScenarioProjectUserResponse extends ArrayList<ScenarioProjectUserInformation> {
    private List<ScenarioProjectUserInformation> items;
}
