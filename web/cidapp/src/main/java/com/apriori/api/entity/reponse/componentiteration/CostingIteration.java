package com.apriori.api.entity.reponse.componentiteration;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "newcid/ComponentIterationsResponse.json")
public class CostingIteration {

    @JsonProperty("response")
    private CostingIteration response;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("createdAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime createdAt;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("updatedAt")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime updatedAt;
    @JsonProperty("createdByName")
    private String createdByName;
    @JsonProperty("customerIdentity")
    private String customerIdentity;
    @JsonProperty("iteration")
    private Integer iteration;
    @JsonProperty("scenarioIterationKey")
    private String scenarioIterationKey;
    @JsonProperty("costingInput")
    private CostingInput costingInput;
    @JsonProperty("scenarioAnalysis")
    private ScenarioAnalysis scenarioAnalysis;
    @JsonProperty("scenarioCustomAttributes")
    private List<ScenarioCustomAttribute> scenarioCustomAttributes = null;
    @JsonProperty("scenarioDtcIssues")
    private List<ScenarioDtcIssue> scenarioDtcIssues = null;
    @JsonProperty("scenarioGcd")
    private ScenarioGcd scenarioGcd;
    @JsonProperty("scenarioMetadata")
    private ScenarioMetadata scenarioMetadata;
    @JsonProperty("scenarioProcesses")
    private List<ScenarioProcess> scenarioProcesses = null;
    @JsonProperty("hasThumbnail")
    private Boolean hasThumbnail;
    @JsonProperty("hasWebImage")
    private Boolean hasWebImage;
    @JsonProperty("thumbnail")
    private Thumbnail thumbnail;
    @JsonProperty("material")
    private Material material;

    public CostingIteration getResponse() {
        return this.response;
    }

    public CostingIteration setResponse(CostingIteration response) {
        this.response = response;
        return this;
    }

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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public void setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public String getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public void setScenarioIterationKey(String scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
    }

    public CostingInput getCostingInput() {
        return costingInput;
    }

    public void setCostingInput(CostingInput costingInput) {
        this.costingInput = costingInput;
    }

    public ScenarioAnalysis getScenarioAnalysis() {
        return scenarioAnalysis;
    }

    public void setScenarioAnalysis(ScenarioAnalysis scenarioAnalysis) {
        this.scenarioAnalysis = scenarioAnalysis;
    }

    public List<ScenarioCustomAttribute> getScenarioCustomAttributes() {
        return scenarioCustomAttributes;
    }

    public void setScenarioCustomAttributes(List<ScenarioCustomAttribute> scenarioCustomAttributes) {
        this.scenarioCustomAttributes = scenarioCustomAttributes;
    }

    public List<ScenarioDtcIssue> getScenarioDtcIssues() {
        return scenarioDtcIssues;
    }

    public void setScenarioDtcIssues(List<ScenarioDtcIssue> scenarioDtcIssues) {
        this.scenarioDtcIssues = scenarioDtcIssues;
    }

    public ScenarioGcd getScenarioGcd() {
        return scenarioGcd;
    }

    public void setScenarioGcd(ScenarioGcd scenarioGcd) {
        this.scenarioGcd = scenarioGcd;
    }

    public ScenarioMetadata getScenarioMetadata() {
        return scenarioMetadata;
    }

    public void setScenarioMetadata(ScenarioMetadata scenarioMetadata) {
        this.scenarioMetadata = scenarioMetadata;
    }

    public List<ScenarioProcess> getScenarioProcesses() {
        return scenarioProcesses;
    }

    public void setScenarioProcesses(List<ScenarioProcess> scenarioProcesses) {
        this.scenarioProcesses = scenarioProcesses;
    }

    public Boolean getHasThumbnail() {
        return hasThumbnail;
    }

    public void setHasThumbnail(Boolean hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
    }

    public Boolean getHasWebImage() {
        return hasWebImage;
    }

    public void setHasWebImage(Boolean hasWebImage) {
        this.hasWebImage = hasWebImage;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
