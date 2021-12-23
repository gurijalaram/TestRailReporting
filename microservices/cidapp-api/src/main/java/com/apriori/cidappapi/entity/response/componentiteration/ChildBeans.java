package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChildBeans {
    private String category;
    private Integer sizeCount;
    private Integer gcdCount;
    private List<ChildBeans_> childBeans;
    private ArtifactKey artifactKey;
}
