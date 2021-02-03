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
    public void setActiveAxes(List<ActiveAxis> activeAxes) {
        this.activeAxes = activeAxes;
    }

    @JsonProperty("activeDimensions")
    public ActiveDimensions getActiveDimensions() {
        return activeDimensions;
    }

    @JsonProperty("activeDimensions")
    public void setActiveDimensions(ActiveDimensions activeDimensions) {
        this.activeDimensions = activeDimensions;
    }

    @JsonProperty("axesEntries")
    public List<AxesEntries> getAxesEntries() {
        return axesEntries;
    }

    @JsonProperty("axesEntries")
    public void setAxesEntries(List<AxesEntries> axesEntries) {
        this.axesEntries = axesEntries;
    }

    @JsonProperty("boundingBox")
    public List<Integer> getBoundingBox() {
        return boundingBox;
    }

    @JsonProperty("boundingBox")
    public void setBoundingBox(List<Integer> boundingBox) {
        this.boundingBox = boundingBox;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("createdByName")
    public String getCreatedByName() {
        return createdByName;
    }

    @JsonProperty("createdByName")
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @JsonProperty("deletedAt")
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @JsonProperty("deletedAt")
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @JsonProperty("deletedBy")
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonProperty("deletedBy")
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @JsonProperty("deletedByName")
    public String getDeletedByName() {
        return deletedByName;
    }

    @JsonProperty("deletedByName")
    public void setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
    }

    @JsonProperty("drawableNodes")
    public List<DrawableNode> getDrawableNodes() {
        return drawableNodes;
    }

    @JsonProperty("drawableNodes")
    public void setDrawableNodes(List<DrawableNode> drawableNodes) {
        this.drawableNodes = drawableNodes;
    }

    @JsonProperty("faceIndices")
    public List<FaceIndex> getFaceIndices() {
        return faceIndices;
    }

    @JsonProperty("faceIndices")
    public void setFaceIndices(List<FaceIndex> faceIndices) {
        this.faceIndices = faceIndices;
    }

    @JsonProperty("identity")
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("identity")
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @JsonProperty("nodeEntries")
    public List<NodeEntry> getNodeEntries() {
        return nodeEntries;
    }

    @JsonProperty("nodeEntries")
    public void setNodeEntries(List<NodeEntry> nodeEntries) {
        this.nodeEntries = nodeEntries;
    }

    @JsonProperty("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @JsonProperty("updatedBy")
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @JsonProperty("updatedByName")
    public String getUpdatedByName() {
        return updatedByName;
    }

    @JsonProperty("updatedByName")
    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }
}
