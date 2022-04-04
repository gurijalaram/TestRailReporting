package com.apriori.cidappapi.entity.builder;

import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

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
    private String scenarioName;
    private List<String> scenarioNames;
    private File resourceFile;
    private List<File> resourceFiles;
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
    private List<ScenarioItem> scenarioItems;
    private PostComponentResponse component;
}
