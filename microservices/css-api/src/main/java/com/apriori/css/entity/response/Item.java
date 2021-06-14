package com.apriori.css.entity.response;

import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    private String componentConfigurationName;
    private String subjectIdentity;
    private List<String> permissions;
    private String customerIdentity;
    private String componentIdentity;
    private String componentName;
    private String componentType;
    private String componentFilename;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime componentCreatedAt;
    private String componentCreatedBy;
    private String componentCreatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime componentUpdatedAt;
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
    private Boolean scenarioSystemLocked;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime scenarioCreatedAt;
    private String scenarioCreatedBy;
    private String scenarioCreatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime scenarioUpdatedAt;
    private String iterationIdentity;
    private Integer iteration;
    private Boolean latest;
    private String scenarioIterationKey;
    private Boolean iterationHasWebImage;
    private Boolean iterationIsCadConnected;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime iterationCreatedAt;
    private String iterationCreatedBy;
    private String iterationCreatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmZ.class)
    private LocalDateTime iterationUpdatedAt;
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