package com.apriori.builder;

import com.apriori.enums.ProcessGroupEnum;
import com.apriori.models.request.component.PublishRequest;
import com.apriori.models.response.component.CostingTemplate;
import com.apriori.models.response.component.PostComponentResponse;
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

    public CostingTemplate getCostingTemplate() {
        if (costingTemplate == null) {
            costingTemplate = CostingTemplate.builder()
                .processGroupName(processGroup.getProcessGroup())
                .build();
        }
        return costingTemplate;
    }
}
