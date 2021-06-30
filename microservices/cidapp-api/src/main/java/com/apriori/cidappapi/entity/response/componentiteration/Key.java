
package com.apriori.cidappapi.entity.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Key {
    private String artifactTypeName;
    private Integer sequenceNumber;
    private String displayName;

    public String getArtifactTypeName() {
        return artifactTypeName;
    }

    public Key setArtifactTypeName(String artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public Key setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Key setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}