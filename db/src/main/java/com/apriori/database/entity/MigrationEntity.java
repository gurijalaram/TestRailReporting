package com.apriori.database.entity;

import com.fbc.datamodel.shared.ScenarioType;

public class MigrationEntity {

    private ScenarioType scenarioType;
    private String elementName;
    private String scenarioName;
    private String newScenarioName;


    public static MigrationEntity init(final ScenarioType scenarioType, final String elementName, final String scenarioName) {
        return new MigrationEntity(scenarioType, elementName, scenarioName, scenarioName);
    }

    public static MigrationEntity initWithNewScenarioNameForMigration(final ScenarioType scenarioType, final String elementName, final String scenarioName, final String newScenarioName) {
        return new MigrationEntity(scenarioType, elementName, scenarioName, newScenarioName);
    }

    private MigrationEntity(ScenarioType scenarioType, String elementName, String scenarioName, String newScenarioName) {
        this.scenarioType = scenarioType;
        this.elementName = elementName;
        this.scenarioName = scenarioName;
        this.newScenarioName = newScenarioName;
    }

    public ScenarioType getScenarioType() {
        return scenarioType;
    }

    public MigrationEntity setScenarioType(ScenarioType scenarioType) {
        this.scenarioType = scenarioType;
        return this;
    }

    public String getElementName() {
        return elementName;
    }

    public MigrationEntity setElementName(String elementName) {
        this.elementName = elementName;
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public MigrationEntity setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public String getNewScenarioName() {
        return newScenarioName;
    }

    public MigrationEntity setNewScenarioName(String newScenarioName) {
        this.newScenarioName = newScenarioName;
        return this;
    }
}
