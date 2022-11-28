package com.apriori.cidappapi.entity.builder;

import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;

@Builder
@Data
public class ComponentInfoBuilder {
    private final UserCredentials user;
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
    private final ScenarioStateEnum scenarioState = ScenarioStateEnum.COST_COMPLETE;
    private List<ComponentInfoBuilder> subComponents;
    private ScenarioItem scenarioItem;
    private List<ScenarioItem> scenarioItems;
    private PostComponentResponse component;
    private PublishRequest publishRequest;
    @Builder.Default
    private final boolean overrideScenario = false;
    private String costingTemplateId;
    private CostingTemplate costingTemplate;
}
