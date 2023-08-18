package com.apriori.cidappapi.models.response.componentiteration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Data_ {
    private Integer sizeCount;
    private List<ChildBeans> childBeans;
    private ArtifactKey_ artifactKey;
    private String category;
    private Integer gcdCount;
}