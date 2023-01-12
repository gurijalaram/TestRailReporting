package com.apriori.cidappapi.entity.request;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;

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
