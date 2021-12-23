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
public class ScenarioDesignInvestigations {
    private String identity;
    private List<Data_> data;
    private String investigationType;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class Data_ {
        private Integer sizeCount;
        private List<ChildBeans> childBeans;
        private ArtifactKey_ artifactKey;
        private String category;
        private Integer gcdCount;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ChildBeans {
        private String category;
        private Integer sizeCount;
        private Integer gcdCount;
        private List<ChildBeans_> childBeans;
        private ArtifactKey artifactKey;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ChildBeans_ {
        private String category;
        private Integer sizeCount;
        private Integer gcdCount;
        private ArtifactKey__ artifactKey;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ArtifactKey {
        private String artifactTypeName;
        private Integer sequenceNumber;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ArtifactKey_ {
        private String artifactTypeName;
        private Integer sequenceNumber;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ArtifactKey__ {
        private String artifactTypeName;
        private Integer sequenceNumber;
        private String displayName;
    }
}
