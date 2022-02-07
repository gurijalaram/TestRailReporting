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
    private final String componentId;
    private final String scenarioId;
    private final ProcessGroupEnum processGroup;
    /**
     * Setting to default as this field is currently not used in some tests. Sometimes a component can be costed without changing the fields,
     * so it is very possible that every field in this pojo should have a default value
     */
    @Builder.Default
    @JsonProperty("vpeName")
    private final DigitalFactoryEnum digitalFactory = DigitalFactoryEnum.APRIORI_USA;
    private final String mode;
    private final String material;
    private final UserCredentials user;
    @Builder.Default
    private final ScenarioStateEnum scenarioState = ScenarioStateEnum.COST_COMPLETE;
}
