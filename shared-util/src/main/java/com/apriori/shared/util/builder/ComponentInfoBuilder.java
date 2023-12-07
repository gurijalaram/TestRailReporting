package com.apriori.shared.util.builder;

import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.request.component.ComponentRequest;
import com.apriori.shared.util.models.request.component.PublishRequest;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.models.response.component.PostComponentResponse;

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
    private Metadata metadata;
    private ComponentRequest componentRequest;


    public CostingTemplate getCostingTemplate() {
        if (costingTemplate == null) {
            costingTemplate = CostingTemplate.builder()
                .processGroupName(processGroup.getProcessGroup())
                .build();
        }
        return costingTemplate;
    }
}
