package com.apriori.cidappapi.models.request;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.models.request.component.PublishRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPublishRequest {
    private ComponentInfoBuilder componentInfo;
    private PublishRequest publishRequest;
    @Builder.Default
    private Boolean scenarioPublished = false;
}
