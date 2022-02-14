package com.apriori.cidappapi.entity.builder;

import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComponentInfoBuilder {
    private final String componentName;
    private final String extension;
    private final String scenarioName;
    private String componentId;
    private String scenarioId;
    private final ProcessGroupEnum processGroup;
    @Builder.Default
    @JsonProperty("vpeName")
    private final DigitalFactoryEnum digitalFactory = DigitalFactoryEnum.APRIORI_USA;
    private final String mode;
    private final String material;
    private final UserCredentials user;
    @Builder.Default
    private final ScenarioStateEnum scenarioState = ScenarioStateEnum.COST_COMPLETE;
}
