package com.apriori.cidappapi.models.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
