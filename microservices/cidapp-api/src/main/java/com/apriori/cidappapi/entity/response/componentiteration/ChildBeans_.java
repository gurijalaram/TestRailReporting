package com.apriori.cidappapi.entity.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChildBeans_ {
    private String category;
    private Integer sizeCount;
    private Integer gcdCount;
    private ArtifactKey__ artifactKey;
}
