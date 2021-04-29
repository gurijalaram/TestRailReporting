package com.apriori.entity.request.cost.createcostworkorder;

public class CostOrderInputs {
    private Integer inputSetId;
    private CostOrderScenarioIteration scenarioIterationKey;
    private boolean keepFreeBodies;
    private boolean freeBodiesPreserveCad;
    private boolean freeBodiesIgnoreMissingComponents;

    public CostOrderInputs() {
        keepFreeBodies = false;
        freeBodiesPreserveCad = false;
        freeBodiesIgnoreMissingComponents = true;
    }

    public Integer getInputSetId() {
        return inputSetId;
    }

    public CostOrderInputs setInputSetId(Integer inputSetId) {
        this.inputSetId = inputSetId;
        return this;
    }

    public CostOrderScenarioIteration getScenarioIterationKey() {
        return scenarioIterationKey;
    }

    public CostOrderInputs setScenarioIterationKey(CostOrderScenarioIteration scenarioIterationKey) {
        this.scenarioIterationKey = scenarioIterationKey;
        return this;
    }

    public boolean isKeepFreeBodies() {
        return keepFreeBodies;
    }

    public void setKeepFreeBodies(boolean keepFreeBodies) {
        this.keepFreeBodies = keepFreeBodies;
    }

    public boolean isFreeBodiesPreserveCad() {
        return freeBodiesPreserveCad;
    }

    public void setFreeBodiesPreserveCad(boolean freeBodiesPreserveCad) {
        this.freeBodiesPreserveCad = freeBodiesPreserveCad;
    }

    public boolean isFreeBodiesIgnoreMissingComponents() {
        return freeBodiesIgnoreMissingComponents;
    }

    public void setFreeBodiesIgnoreMissingComponents(boolean freeBodiesIgnoreMissingComponents) {
        this.freeBodiesIgnoreMissingComponents = freeBodiesIgnoreMissingComponents;
    }
}