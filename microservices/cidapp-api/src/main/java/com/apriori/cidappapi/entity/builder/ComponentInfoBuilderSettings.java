package com.apriori.cidappapi.entity.builder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ComponentInfoBuilderSettings {
    @Builder.Default
    private Boolean useEmptyCostingTemplateID = false;
}
