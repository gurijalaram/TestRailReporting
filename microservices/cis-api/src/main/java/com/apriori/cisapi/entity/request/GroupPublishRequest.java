package com.apriori.cisapi.entity.request;

import com.apriori.entity.builder.ComponentInfoBuilder;

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
    private int workspaceId;
}
