package com.apriori.cidappapi.builder;

import com.apriori.cidappapi.models.request.PublishRequest;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.models.response.PostComponentResponse;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.reader.file.user.UserCredentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentInfoBuilder implements Serializable {
    private UserCredentials user;
    private String componentName;
    private String extension;
    private String scenarioName;
    private File resourceFile;
    private List<File> resourceFiles;
    private String componentIdentity;
    private String scenarioIdentity;
    private ProcessGroupEnum processGroup;
    private List<ComponentInfoBuilder> subComponents;
    private PostComponentResponse component;
    private PublishRequest publishRequest;
    @Builder.Default
    private Boolean overrideScenario = false;
    private CostingTemplate costingTemplate;
}
