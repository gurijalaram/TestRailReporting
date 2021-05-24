package com.apriori.cidapp.entity.response.componentiteration;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cidapp/ComponentIterationResponse.json")
public class ComponentIteration {
    private ComponentIteration response;
    private Boolean isCadConnected;
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
    private List<Object> scenarioCustomAttributes = null;
    private List<ScenarioDtcIssue> scenarioDtcIssues = null;
    private ScenarioGcd scenarioGcd;
    private ScenarioMetadata scenarioMetadata;
    private List<ScenarioProcess> scenarioProcesses = null;
    private AnalysisOfChildren analysisOfChildren;
    private AnalysisOfScenario analysisOfScenario;
    private AnalysisOfScenarioAndChildren analysisOfScenarioAndChildren;
    private PartNestingDiagram partNestingDiagram;
    private Boolean hasThumbnail;
    private Boolean hasWebImage;
    private Thumbnail thumbnail;
    private Material material;
    private MaterialStock materialStock;

    public ComponentIteration getResponse() {
        return this.response;
    }

    public Boolean getIsCadConnected() {
        return isCadConnected;
    }

    public ComponentIteration setIsCadConnected(Boolean isCadConnected) {
        this.isCadConnected = isCadConnected;
        return this;
    }

    public String getIdentity() {
        return identity;
    }

    public ComponentIteration setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ComponentIteration setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ComponentIteration setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ComponentIteration setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public ComponentIteration setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public ComponentIteration setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
        return this;
    }

    public Integer getIteration() {
        return iteration;
    }

    public ComponentIteration setIteration(Integer iteration) {
        this.iteration = iteration;
        return this;
    }

    public String getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public ComponentIteration setScenarioIterationKey(String scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public CostingInput getCostingInput() {
        return costingInput;
    }

    public ComponentIteration setCostingInput(CostingInput costingInput) {
        this.costingInput = costingInput;
        return this;
    }

    public List<Object> getScenarioCustomAttributes() {
        return scenarioCustomAttributes;
    }

    public ComponentIteration setScenarioCustomAttributes(List<Object> scenarioCustomAttributes) {
        this.scenarioCustomAttributes = scenarioCustomAttributes;
        return this;
    }

    public List<ScenarioDtcIssue> getScenarioDtcIssues() {
        return scenarioDtcIssues;
    }

    public ComponentIteration setScenarioDtcIssues(List<ScenarioDtcIssue> scenarioDtcIssues) {
        this.scenarioDtcIssues = scenarioDtcIssues;
        return this;
    }

    public ScenarioGcd getScenarioGcd() {
        return scenarioGcd;
    }

    public ComponentIteration setScenarioGcd(ScenarioGcd scenarioGcd) {
        this.scenarioGcd = scenarioGcd;
        return this;
    }

    public ScenarioMetadata getScenarioMetadata() {
        return scenarioMetadata;
    }

    public ComponentIteration setScenarioMetadata(ScenarioMetadata scenarioMetadata) {
        this.scenarioMetadata = scenarioMetadata;
        return this;
    }

    public List<ScenarioProcess> getScenarioProcesses() {
        return scenarioProcesses;
    }

    public ComponentIteration setScenarioProcesses(List<ScenarioProcess> scenarioProcesses) {
        this.scenarioProcesses = scenarioProcesses;
        return this;
    }

    public AnalysisOfChildren getAnalysisOfChildren() {
        return analysisOfChildren;
    }

    public ComponentIteration setAnalysisOfChildren(AnalysisOfChildren analysisOfChildren) {
        this.analysisOfChildren = analysisOfChildren;
        return this;
    }

    public AnalysisOfScenario getAnalysisOfScenario() {
        return analysisOfScenario;
    }

    public ComponentIteration setAnalysisOfScenario(AnalysisOfScenario analysisOfScenario) {
        this.analysisOfScenario = analysisOfScenario;
        return this;
    }

    public AnalysisOfScenarioAndChildren getAnalysisOfScenarioAndChildren() {
        return analysisOfScenarioAndChildren;
    }

    public ComponentIteration setAnalysisOfScenarioAndChildren(AnalysisOfScenarioAndChildren analysisOfScenarioAndChildren) {
        this.analysisOfScenarioAndChildren = analysisOfScenarioAndChildren;
        return this;
    }

    public PartNestingDiagram getPartNestingDiagram() {
        return partNestingDiagram;
    }

    public ComponentIteration setPartNestingDiagram(PartNestingDiagram partNestingDiagram) {
        this.partNestingDiagram = partNestingDiagram;
        return this;
    }

    public Boolean getHasThumbnail() {
        return hasThumbnail;
    }

    public ComponentIteration setHasThumbnail(Boolean hasThumbnail) {
        this.hasThumbnail = hasThumbnail;
        return this;
    }

    public Boolean getHasWebImage() {
        return hasWebImage;
    }

    public ComponentIteration setHasWebImage(Boolean hasWebImage) {
        this.hasWebImage = hasWebImage;
        return this;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public ComponentIteration setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public ComponentIteration setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public MaterialStock getMaterialStock() {
        return materialStock;
    }

    public ComponentIteration setMaterialStock(MaterialStock materialStock) {
        this.materialStock = materialStock;
        return this;
    }

    static class AnalysisOfScenarioAndChildren {
    }

    static class AnalysisOfChildren {
    }
}
