package com.apriori.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Key_ {
    private String artifactTypeName;
    private Integer sequenceNumber;
    private String displayName;

    public String getArtifactTypeName() {
        return artifactTypeName;
    }

    public Key_ setArtifactTypeName(String artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public Key_ setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Key_ setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}
