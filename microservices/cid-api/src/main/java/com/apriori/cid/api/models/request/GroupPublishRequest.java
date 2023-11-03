package com.apriori.cid.api.models.request;

import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.models.request.component.PublishRequest;

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
