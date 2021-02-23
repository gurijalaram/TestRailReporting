package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioMetadata {
    private String identity;
    private List<Object> activeAxes = null;
    private ActiveDimensions activeDimensions;
    private List<AxesEntry> axesEntries = null;
    private List<Double> boundingBox = null;
    private List<DrawableNode> drawableNodes = null;
    private List<FaceIndex> faceIndices = null;
    private List<Object> nodeEntries = null;

    public String getIdentity() {
        return identity;
    }

    public ScenarioMetadata setIdentity(String identity) {
        this.identity = identity;
    return this;
}

    public List<Object> getActiveAxes() {
        return activeAxes;
    }

    public ScenarioMetadata setActiveAxes(List<Object> activeAxes) {
        this.activeAxes = activeAxes;
    return this;
}

    public ActiveDimensions getActiveDimensions() {
        return activeDimensions;
    }

    public ScenarioMetadata setActiveDimensions(ActiveDimensions activeDimensions) {
        this.activeDimensions = activeDimensions;
    return this;
}

    public List<AxesEntry> getAxesEntries() {
        return axesEntries;
    }

    public ScenarioMetadata setAxesEntries(List<AxesEntry> axesEntries) {
        this.axesEntries = axesEntries;
    return this;
}

    public List<Double> getBoundingBox() {
        return boundingBox;
    }

    public ScenarioMetadata setBoundingBox(List<Double> boundingBox) {
        this.boundingBox = boundingBox;
    return this;
}

    public List<DrawableNode> getDrawableNodes() {
        return drawableNodes;
    }

    public ScenarioMetadata setDrawableNodes(List<DrawableNode> drawableNodes) {
        this.drawableNodes = drawableNodes;
    return this;
}

    public List<FaceIndex> getFaceIndices() {
        return faceIndices;
    }

    public ScenarioMetadata setFaceIndices(List<FaceIndex> faceIndices) {
        this.faceIndices = faceIndices;
    return this;
}

    public List<Object> getNodeEntries() {
        return nodeEntries;
    }

    public ScenarioMetadata setNodeEntries(List<Object> nodeEntries) {
        this.nodeEntries = nodeEntries;
    return this;
}
}
