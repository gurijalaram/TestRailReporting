package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class ScenarioMetadata {

    @JsonProperty("activeAxes")
    private List<ActiveAxis> activeAxes;
    @JsonProperty("activeDimensions")
    private ActiveDimensions activeDimensions;
    @JsonProperty("axesEntries")
    private List<AxesEntries> axesEntries;
    @JsonProperty("boundingBox")
    private List<Integer> boundingBox;
    @JsonProperty("createdAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("createdByName")
    private String createdByName;
    @JsonProperty("deletedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    @JsonProperty("deletedBy")
    private String deletedBy;
    @JsonProperty("deletedByName")
    private String deletedByName;
    @JsonProperty("drawableNodes")
    private List<DrawableNode> drawableNodes;
    @JsonProperty("faceIndices")
    private List<FaceIndex> faceIndices;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("nodeEntries")
    private List<NodeEntry> nodeEntries;
    @JsonProperty("updatedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty("updatedBy")
    private String updatedBy;
    @JsonProperty("updatedByName")
    private String updatedByName;

    @JsonProperty("activeAxes")
    public List<ActiveAxis> getActiveAxes() {
        return activeAxes;
    }

    @JsonProperty("activeAxes")
    public ScenarioMetadata setActiveAxes(List<ActiveAxis> activeAxes) {
        this.activeAxes = activeAxes;
        return this;
    }

    @JsonProperty("activeDimensions")
    public ActiveDimensions getActiveDimensions() {
        return activeDimensions;
    }

    @JsonProperty("activeDimensions")
    public ScenarioMetadata setActiveDimensions(ActiveDimensions activeDimensions) {
        this.activeDimensions = activeDimensions;
        return this;
    }

    @JsonProperty("axesEntries")
    public List<AxesEntries> getAxesEntries() {
        return axesEntries;
    }

    @JsonProperty("axesEntries")
    public ScenarioMetadata setAxesEntries(List<AxesEntries> axesEntries) {
        this.axesEntries = axesEntries;
        return this;
    }

    @JsonProperty("boundingBox")
    public List<Integer> getBoundingBox() {
        return boundingBox;
    }

    @JsonProperty("boundingBox")
    public ScenarioMetadata setBoundingBox(List<Integer> boundingBox) {
        this.boundingBox = boundingBox;
        return this;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public ScenarioMetadata setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public ScenarioMetadata setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public ScenarioMetadata setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    @JsonProperty("deletedAt")
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public ScenarioMetadata setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    @JsonProperty("deletedBy")
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonProperty("deletedBy")
    public ScenarioMetadata setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
        return this;
    }

    @JsonProperty("deletedByName")
    public String getDeletedByName() {
        return deletedByName;
    }

    @JsonProperty("deletedByName")
    public ScenarioMetadata setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
        return this;
    }

    @JsonProperty("drawableNodes")
    public List<DrawableNode> getDrawableNodes() {
        return drawableNodes;
    }

    @JsonProperty("drawableNodes")
    public ScenarioMetadata setDrawableNodes(List<DrawableNode> drawableNodes) {
        this.drawableNodes = drawableNodes;
        return this;
    }

    @JsonProperty("faceIndices")
    public List<FaceIndex> getFaceIndices() {
        return faceIndices;
    }

    @JsonProperty("faceIndices")
    public ScenarioMetadata setFaceIndices(List<FaceIndex> faceIndices) {
        this.faceIndices = faceIndices;
        return this;
    }

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public ScenarioMetadata setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    @JsonProperty("nodeEntries")
    public List<NodeEntry> getNodeEntries() {
        return nodeEntries;
    }

    @JsonProperty("nodeEntries")
    public ScenarioMetadata setNodeEntries(List<NodeEntry> nodeEntries) {
        this.nodeEntries = nodeEntries;
        return this;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public ScenarioMetadata setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public ScenarioMetadata setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public ScenarioMetadata setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
        return this;
    }
}
