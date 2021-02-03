package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

public class ScenarioDtcIssue {

    private String attributeId;
    private String attributeText;
    private String category;
    private String categoryText;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    private Integer currentValue;
    private String currentValueText;
    private Integer decimalPlaces;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String dtcIssueType;
    private String gcdArtifactTypeName;
    private String gcdDisplayName;
    private Integer gcdSequenceNumber;
    private String identity;
    private String messageId;
    private String messageText;
    private Integer priority;
    private String processDisplayName;
    private String processGroupName;
    private Integer processIndex;
    private String processName;
    private String rule;
    private Integer suggestedMinValue;
    private String suggestedMinValueText;
    private String type;
    private String unitType;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByName;
    private String vpeName;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeText() {
        return attributeText;
    }

    public void setAttributeText(String attributeText) {
        this.attributeText = attributeText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public String getCurrentValueText() {
        return currentValueText;
    }

    public void setCurrentValueText(String currentValueText) {
        this.currentValueText = currentValueText;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getDeletedByName() {
        return deletedByName;
    }

    public void setDeletedByName(String deletedByName) {
        this.deletedByName = deletedByName;
    }

    public String getDtcIssueType() {
        return dtcIssueType;
    }

    public void setDtcIssueType(String dtcIssueType) {
        this.dtcIssueType = dtcIssueType;
    }

    public String getGcdArtifactTypeName() {
        return gcdArtifactTypeName;
    }

    public void setGcdArtifactTypeName(String gcdArtifactTypeName) {
        this.gcdArtifactTypeName = gcdArtifactTypeName;
    }

    public String getGcdDisplayName() {
        return gcdDisplayName;
    }

    public void setGcdDisplayName(String gcdDisplayName) {
        this.gcdDisplayName = gcdDisplayName;
    }

    public Integer getGcdSequenceNumber() {
        return gcdSequenceNumber;
    }

    public void setGcdSequenceNumber(Integer gcdSequenceNumber) {
        this.gcdSequenceNumber = gcdSequenceNumber;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getProcessDisplayName() {
        return processDisplayName;
    }

    public void setProcessDisplayName(String processDisplayName) {
        this.processDisplayName = processDisplayName;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public void setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
    }

    public Integer getProcessIndex() {
        return processIndex;
    }

    public void setProcessIndex(Integer processIndex) {
        this.processIndex = processIndex;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getSuggestedMinValue() {
        return suggestedMinValue;
    }

    public void setSuggestedMinValue(Integer suggestedMinValue) {
        this.suggestedMinValue = suggestedMinValue;
    }

    public String getSuggestedMinValueText() {
        return suggestedMinValueText;
    }

    public void setSuggestedMinValueText(String suggestedMinValueText) {
        this.suggestedMinValueText = suggestedMinValueText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public String getVpeName() {
        return vpeName;
    }

    public void setVpeName(String vpeName) {
        this.vpeName = vpeName;
    }
}
