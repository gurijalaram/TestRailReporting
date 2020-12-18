package com.apriori.apibase.services.response.objects;

import com.apriori.utils.enums.WorkOrderScenarioTypeEnum;
import com.apriori.utils.enums.WorkspaceEnum;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WorkOrderRequestEntity {

    private String baseURL = System.getProperty("baseURL");
    private String username;
    private String password;
    private String scenarioName;
    private WorkSpaceSearchWrapper workSpaceSearchWrapper;
    private List<WorkspaceEnum> workspace = Collections.singletonList(WorkspaceEnum.PRIVATE_API);
    private WorkOrderScenarioTypeEnum scenarioType = WorkOrderScenarioTypeEnum.PART;
    private boolean includeAllStateIterations = false;

    public static WorkOrderRequestEntity defaultRequestByCustomCredentials(String username, String password, String scenarioName) {
        return new WorkOrderRequestEntity(username, password, scenarioName);
    }

    public WorkOrderRequestEntity(String username, String password, String scenarioName) {
        this.username = username;
        this.password = password;
        this.scenarioName = scenarioName;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public WorkOrderRequestEntity setBaseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    public boolean isIncludeAllStateIterations() {
        return includeAllStateIterations;
    }

    public WorkOrderRequestEntity setIncludeAllStateIterations(boolean includeAllStateIterations) {
        this.includeAllStateIterations = includeAllStateIterations;
        return this;
    }

    public WorkOrderScenarioTypeEnum getScenarioType() {
        return scenarioType;
    }

    public WorkOrderRequestEntity setScenarioType(WorkOrderScenarioTypeEnum scenarioType) {
        this.scenarioType = scenarioType;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public WorkOrderRequestEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public WorkOrderRequestEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<String> getWorkspace() {
        return workspace.stream().map(WorkspaceEnum::getWorkspace).collect(Collectors.toList());
    }

    public WorkOrderRequestEntity setWorkspace(WorkspaceEnum workspace) {
        this.workspace = Collections.singletonList(workspace);
        return this;
    }

    public WorkOrderRequestEntity addWorkspace(WorkspaceEnum workspace) {
        this.workspace.add(workspace);
        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public WorkOrderRequestEntity setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
        return this;
    }

    public WorkSpaceSearchWrapper getWorkSpaceSearchWrapper() {
        return workSpaceSearchWrapper;
    }

    public WorkOrderRequestEntity setWorkSpaceSearchWrapper(WorkSpaceSearchWrapper workSpaceSearchWrapper) {
        this.workSpaceSearchWrapper = workSpaceSearchWrapper;
        return this;
    }
}
