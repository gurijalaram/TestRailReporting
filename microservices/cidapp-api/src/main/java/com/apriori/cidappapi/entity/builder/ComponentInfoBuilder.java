package com.apriori.cidappapi.entity.builder;

import com.apriori.cidappapi.entity.request.PublishRequest;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.entity.response.PostComponentResponse;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.reader.file.user.UserCredentials;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class ComponentInfoBuilder implements Serializable {
    private final UserCredentials user;
    private final String componentName;
    private final String extension;
    private String scenarioName;
    private File resourceFile;
    private List<File> resourceFiles;
    private String componentIdentity;
    private String scenarioIdentity;
    private final ProcessGroupEnum processGroup;
    private List<ComponentInfoBuilder> subComponents;
    private PostComponentResponse component;
    private PublishRequest publishRequest;
    @Builder.Default
    private final boolean overrideScenario = false;
    private CostingTemplate costingTemplate;
}
