package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.utils.http.enums.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(location = "ScenarioProjectUsersResponseSchema.json")
public class ScenarioProjectUserResponse extends ArrayList<ScenarioProjectUserInformation> {
     private List<ScenarioProjectUserInformation> items = new ArrayList<>();
  }
