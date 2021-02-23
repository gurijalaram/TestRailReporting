package com.apriori.entity.reponse.componentiteration.newcostingciene;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "newcid/ComponentIterationsResponse.json")
public class CostingIteration {
    private CostingIteration response;
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
    private Boolean hasThumbnail;
    private Boolean hasWebImage;
    private Thumbnail thumbnail;
    private Material material;
    private MaterialStock materialStock;

    public CostingIteration getResponse() {
        return this.response;
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

    public List<Object> getScenarioCustomAttributes() {
        return scenarioCustomAttributes;
    }

    public CostingIteration setScenarioCustomAttributes(List<Object> scenarioCustomAttributes) {
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

    public AnalysisOfChildren getAnalysisOfChildren() {
        return analysisOfChildren;
    }

    public CostingIteration setAnalysisOfChildren(AnalysisOfChildren analysisOfChildren) {
        this.analysisOfChildren = analysisOfChildren;
        return this;
    }

    public AnalysisOfScenario getAnalysisOfScenario() {
        return analysisOfScenario;
    }

    public CostingIteration setAnalysisOfScenario(AnalysisOfScenario analysisOfScenario) {
        this.analysisOfScenario = analysisOfScenario;
        return this;
    }

    public AnalysisOfScenarioAndChildren getAnalysisOfScenarioAndChildren() {
        return analysisOfScenarioAndChildren;
    }

    public CostingIteration setAnalysisOfScenarioAndChildren(AnalysisOfScenarioAndChildren analysisOfScenarioAndChildren) {
        this.analysisOfScenarioAndChildren = analysisOfScenarioAndChildren;
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

    public MaterialStock getMaterialStock() {
        return materialStock;
    }

    public CostingIteration setMaterialStock(MaterialStock materialStock) {
        this.materialStock = materialStock;
        return this;
    }
}
