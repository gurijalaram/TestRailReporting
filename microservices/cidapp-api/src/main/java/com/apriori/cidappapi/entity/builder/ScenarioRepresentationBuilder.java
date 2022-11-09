package com.apriori.cidappapi.entity.builder;

import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScenarioRepresentationBuilder {
    private final ScenarioItem scenarioItem;
    private final String lastAction;
    private final Boolean published;
    private final UserCredentials user;
}
