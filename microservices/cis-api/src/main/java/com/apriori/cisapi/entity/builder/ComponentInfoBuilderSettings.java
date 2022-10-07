package com.apriori.cisapi.entity.builder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComponentInfoBuilderSettings {
    @Builder.Default
    private Boolean useEmptyComponentID = false;
    @Builder.Default
    private Boolean useEmptyScenarioID = false;
    @Builder.Default
    private Boolean useEmptyCostingTemplateID = false;
}
