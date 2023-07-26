package com.apriori.entity.builder;

import com.apriori.entity.response.ScenarioItem;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.reader.file.user.UserCredentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;

@Builder
@Data
public class ComponentInfoBuilder {
    private final String componentName;
    private final String extension;
    private final String scenarioName;
    private File resourceFile;
    private String componentIdentity;
    private String scenarioIdentity;
    private final ProcessGroupEnum processGroup;
    @Builder.Default
    @JsonProperty("vpeName")
    private final DigitalFactoryEnum digitalFactory = DigitalFactoryEnum.APRIORI_USA;
    @Builder.Default
    private final String mode = "Manual";
    @Builder.Default
    private final String material = "Use Default";
    private final UserCredentials user;
    @Builder.Default
    private final ScenarioStateEnum scenarioState = ScenarioStateEnum.COST_COMPLETE;
    private String assemblyName;
    private List<ComponentInfoBuilder> subComponents;
    private ScenarioItem scenarioItem;
}
