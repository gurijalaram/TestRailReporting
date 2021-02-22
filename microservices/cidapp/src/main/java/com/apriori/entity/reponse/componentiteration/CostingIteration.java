package com.apriori.entity.reponse.componentiteration;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostingIteration {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    private String createdByName;
    private String customerIdentity;
    private Integer iteration;
    private String scenarioIterationKey;
    private CostingInput costingInput;
    private ScenarioAnalysis scenarioAnalysis;
    private List<ScenarioCustomAttribute> scenarioCustomAttributes = null;
    private List<ScenarioDtcIssue> scenarioDtcIssues = null;
    private ScenarioGcd scenarioGcd;
    private ScenarioMetadata scenarioMetadata;
    private List<ScenarioProcess> scenarioProcesses = null;
    private Boolean hasThumbnail;
    private Boolean hasWebImage;
    private Thumbnail thumbnail;
    private Material material;

    public String getIdentity() {
        return identity;
    }

    public CostingIteration setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CostingIteration setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CostingIteration setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CostingIteration setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public CostingIteration setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public CostingIteration setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public CostingIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }

    public String getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostingIteration setScenarioIterationKey(String scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public CostingInput getCostingInput() {
        return costingInput;
    }

    public CostingIteration setCostingInput(CostingInput costingInput) {
        this.costingInput = costingInput;
        return this;
    }

    public ScenarioAnalysis getScenarioAnalysis() {
        return scenarioAnalysis;
    }

    public CostingIteration setScenarioAnalysis(ScenarioAnalysis scenarioAnalysis) {
        this.scenarioAnalysis = scenarioAnalysis;
        return this;
    }

    public List<ScenarioCustomAttribute> getScenarioCustomAttributes() {
        return scenarioCustomAttributes;
    }

    public CostingIteration setScenarioCustomAttributes(List<ScenarioCustomAttribute> scenarioCustomAttributes) {
        this.scenarioCustomAttributes = scenarioCustomAttributes;
        return this;
    }

    public List<ScenarioDtcIssue> getScenarioDtcIssues() {
        return scenarioDtcIssues;
    }

    public CostingIteration setScenarioDtcIssues(List<ScenarioDtcIssue> scenarioDtcIssues) {
        this.scenarioDtcIssues = scenarioDtcIssues;
        return this;
    }

    public ScenarioGcd getScenarioGcd() {
        return scenarioGcd;
    }

    public CostingIteration setScenarioGcd(ScenarioGcd scenarioGcd) {
        this.scenarioGcd = scenarioGcd;
        return this;
    }

    public ScenarioMetadata getScenarioMetadata() {
        return scenarioMetadata;
    }

    public CostingIteration setScenarioMetadata(ScenarioMetadata scenarioMetadata) {
        this.scenarioMetadata = scenarioMetadata;
        return this;
    }

    public List<ScenarioProcess> getScenarioProcesses() {
        return scenarioProcesses;
    }

    public CostingIteration setScenarioProcesses(List<ScenarioProcess> scenarioProcesses) {
        this.scenarioProcesses = scenarioProcesses;
        return this;
    }

    public Boolean getHasThumbnail() {
        return hasThumbnail;
    }

    public CostingIteration setHasThumbnail(Boolean hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
        return this;
    }

    public Boolean getHasWebImage() {
        return hasWebImage;
    }

    public CostingIteration setHasWebImage(Boolean hasWebImage) {
        this.hasWebImage = hasWebImage;
        return this;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public CostingIteration setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public CostingIteration setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
