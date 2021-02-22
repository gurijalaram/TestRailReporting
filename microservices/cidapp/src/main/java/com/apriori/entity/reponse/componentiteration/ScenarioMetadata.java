package com.apriori.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioMetadata {
    private List<ActiveAxis> activeAxes;
    private ActiveDimensions activeDimensions;
    private List<AxesEntries> axesEntries;
    private List<Integer> boundingBox;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private List<DrawableNode> drawableNodes;
    private List<FaceIndex> faceIndices;
    private String identity;
    private List<NodeEntry> nodeEntries;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByName;

    public List<ActiveAxis> getActiveAxes() {
        return activeAxes;
    }

    public ScenarioMetadata setActiveAxes(List<ActiveAxis> activeAxes) {
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

    public List<AxesEntries> getAxesEntries() {
        return axesEntries;
    }

    public ScenarioMetadata setAxesEntries(List<AxesEntries> axesEntries) {
        this.axesEntries = axesEntries;
        return this;
    }

    public List<Integer> getBoundingBox() {
        return boundingBox;
    }

    public ScenarioMetadata setBoundingBox(List<Integer> boundingBox) {
        this.boundingBox = boundingBox;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ScenarioMetadata setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ScenarioMetadata setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public ScenarioMetadata setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public ScenarioMetadata setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public ScenarioMetadata setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    public String getDeletedByName() {
        return deletedByName;
    }

    public ScenarioMetadata setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
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

    public String getIdentity() {
        return identity;
    }

    public ScenarioMetadata setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public List<NodeEntry> getNodeEntries() {
        return nodeEntries;
    }

    public ScenarioMetadata setNodeEntries(List<NodeEntry> nodeEntries) {
        this.nodeEntries = nodeEntries;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ScenarioMetadata setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ScenarioMetadata setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public ScenarioMetadata setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }
}
