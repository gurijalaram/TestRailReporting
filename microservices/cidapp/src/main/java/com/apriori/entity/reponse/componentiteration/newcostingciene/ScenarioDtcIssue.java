package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioDtcIssue {
    private String identity;
    private String attributeId;
    private String attributeText;
    private String category;
    private String categoryText;
    private Integer decimalPlaces;
    private String dtcIssueType;
    private String gcdArtifactTypeName;
    private String gcdDisplayName;
    private Integer gcdSequenceNumber;
    private String messageText;
    private Integer priority;
    private String type;
    private String currentValueText;
    private String messageId;
    private String processDisplayName;
    private String processGroupName;
    private Integer processIndex;
    private String processName;
    private String rule;
    private String suggestedMinValueText;
    private String vpeName;
    private Double currentValue;
    private Double suggestedMinValue;
    private String unitType;

    public String getIdentity() {
        return identity;
    }

    public ScenarioDtcIssue setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public ScenarioDtcIssue setAttributeId(String attributeId) {
        this.attributeId = attributeId;
        return this;
    }

    public String getAttributeText() {
        return attributeText;
    }

    public ScenarioDtcIssue setAttributeText(String attributeText) {
        this.attributeText = attributeText;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ScenarioDtcIssue setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public ScenarioDtcIssue setCategoryText(String categoryText) {
        this.categoryText = categoryText;
        return this;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public ScenarioDtcIssue setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    public String getDtcIssueType() {
        return dtcIssueType;
    }

    public ScenarioDtcIssue setDtcIssueType(String dtcIssueType) {
        this.dtcIssueType = dtcIssueType;
        return this;
    }

    public String getGcdArtifactTypeName() {
        return gcdArtifactTypeName;
    }

    public ScenarioDtcIssue setGcdArtifactTypeName(String gcdArtifactTypeName) {
        this.gcdArtifactTypeName = gcdArtifactTypeName;
        return this;
    }

    public String getGcdDisplayName() {
        return gcdDisplayName;
    }

    public ScenarioDtcIssue setGcdDisplayName(String gcdDisplayName) {
        this.gcdDisplayName = gcdDisplayName;
        return this;
    }

    public Integer getGcdSequenceNumber() {
        return gcdSequenceNumber;
    }

    public ScenarioDtcIssue setGcdSequenceNumber(Integer gcdSequenceNumber) {
        this.gcdSequenceNumber = gcdSequenceNumber;
        return this;
    }

    public String getMessageText() {
        return messageText;
    }

    public ScenarioDtcIssue setMessageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public ScenarioDtcIssue setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public String getType() {
        return type;
    }

    public ScenarioDtcIssue setType(String type) {
        this.type = type;
        return this;
    }

    public String getCurrentValueText() {
        return currentValueText;
    }

    public ScenarioDtcIssue setCurrentValueText(String currentValueText) {
        this.currentValueText = currentValueText;
        return this;
    }

    public String getMessageId() {
        return messageId;
    }

    public ScenarioDtcIssue setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getProcessDisplayName() {
        return processDisplayName;
    }

    public ScenarioDtcIssue setProcessDisplayName(String processDisplayName) {
        this.processDisplayName = processDisplayName;
        return this;
    }

    public String getProcessGroupName() {
        return processGroupName;
    }

    public ScenarioDtcIssue setProcessGroupName(String processGroupName) {
        this.processGroupName = processGroupName;
        return this;
    }

    public Integer getProcessIndex() {
        return processIndex;
    }

    public ScenarioDtcIssue setProcessIndex(Integer processIndex) {
        this.processIndex = processIndex;
        return this;
    }

    public String getProcessName() {
        return processName;
    }

    public ScenarioDtcIssue setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public String getRule() {
        return rule;
    }

    public ScenarioDtcIssue setRule(String rule) {
        this.rule = rule;
        return this;
    }

    public String getSuggestedMinValueText() {
        return suggestedMinValueText;
    }

    public ScenarioDtcIssue setSuggestedMinValueText(String suggestedMinValueText) {
        this.suggestedMinValueText = suggestedMinValueText;
        return this;
    }

    public String getVpeName() {
        return vpeName;
    }

    public ScenarioDtcIssue setVpeName(String vpeName) {
        this.vpeName = vpeName;
        return this;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public ScenarioDtcIssue setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public Double getSuggestedMinValue() {
        return suggestedMinValue;
    }

    public ScenarioDtcIssue setSuggestedMinValue(Double suggestedMinValue) {
        this.suggestedMinValue = suggestedMinValue;
        return this;
    }

    public String getUnitType() {
        return unitType;
    }

    public ScenarioDtcIssue setUnitType(String unitType) {
        this.unitType = unitType;
        return this;
    }
}
