package com.apriori.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActiveAxes {
    private String artifactTypeName;
    private String displayName;
    private Integer sequenceNumber;

    public String getArtifactTypeName() {
        return artifactTypeName;
    }

    public ActiveAxes setArtifactTypeName(String artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ActiveAxes setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public ActiveAxes setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }
}
