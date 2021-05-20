package com.apriori.cidapp.entity.response.css;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    private Item response;
    private String subjectIdentity;
    private List<String> permissions;
    private String customerIdentity;
    private String componentIdentity;
    private String componentName;
    private String componentType;
    private String componentFilename;
    private String componentCreatedAt;
    private String componentCreatedBy;
    private String componentCreatedByName;
    private String componentUpdatedAt;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioType;
    private String scenarioState;
    private String previousScenarioState;
    private String lastAction;
    private String scenarioOwnedBy;
    private String scenarioOwnedByName;
    private Boolean scenarioLocked;
    private Boolean scenarioPublished;
    private String scenarioCreatedAt;
    private String scenarioCreatedBy;
    private String scenarioCreatedByName;
    private String scenarioUpdatedAt;
    private String iterationIdentity;
    private Integer iteration;
    private Boolean latest;
    private String scenarioIterationKey;
    private Boolean iterationHasWebImage;
    private Boolean iterationIsCadConnected;
    private String iterationCreatedAt;
    private String iterationCreatedBy;
    private String iterationCreatedByName;
    private String iterationUpdatedAt;
    private CostingInput costingInput;
    private List<Object> scenarioCustomAttributes;
    private List<Object> scenarioDesignInvestigations;
    private List<Object> scenarioDesignIssues;
    private List<Object> scenarioDesignNotices;
    private ScenarioGcd scenarioGcd;
    private ScenarioMetadata scenarioMetadata;
    private List<Object> scenarioProcesses;
    private AnalysisOfChildren analysisOfChildren;
    private AnalysisOfScenario analysisOfScenario;
    private AnalysisOfScenarioAndChildren analysisOfScenarioAndChildren;
    private PartNestingDiagram partNestingDiagram;
    private Thumbnail thumbnail;

    public static class AnalysisOfChildren {
    }

    public static class AnalysisOfScenario {
    }

    public static class AnalysisOfScenarioAndChildren {
    }

    public static class PartNestingDiagram {
    }

    public static class ScenarioGcd {
    }

    public static class ScenarioMetadata {
    }
}