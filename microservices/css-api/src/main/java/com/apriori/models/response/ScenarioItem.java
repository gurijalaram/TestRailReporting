package com.apriori.models.response;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

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

@Data
@JsonRootName("response")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioItem {
    private String componentConfigurationName;
    private String subjectIdentity;
    private List<String> permissions;
    private String customerIdentity;
    private String componentIdentity;
    private String componentName;
    private String componentDisplayName;
    private String componentType;
    private String componentFilename;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime componentCreatedAt;
    private String componentCreatedBy;
    private String componentCreatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime componentUpdatedAt;
    private String componentUpdatedBy;
    private String componentUpdatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedByName;
    private String identity;
    private String scenarioIdentity;
    private String scenarioName;
    private String scenarioNotes;
    private String scenarioType;
    private String scenarioState;
    private String previousScenarioState;
    private String lastAction;
    private String scenarioOwnedBy;
    private String scenarioOwnedByName;
    private Boolean scenarioLocked;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime scenarioLockedAt;
    private String scenarioLockedBy;
    private String scenarioLockedByName;
    private Boolean scenarioPublished;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime scenarioPublishedAt;
    private String scenarioPublishedBy;
    private String scenarioPublishedByName;
    private Boolean scenarioSystemLocked;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime scenarioCreatedAt;
    private String scenarioCreatedBy;
    private String scenarioCreatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime scenarioUpdatedAt;
    private String iterationIdentity;
    private Integer iteration;
    private String iterationCostingMessage;
    private Boolean latest;
    private ScenarioIterationKey scenarioIterationKey;
    private String  scenarioKey;
    private Boolean iterationHasCustomImage;
    private Boolean iterationHasWebImage;
    private Boolean iterationIsCadConnected;
    private ScenarioGcd scenarioGcd;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime iterationCreatedAt;
    private String iterationCreatedBy;
    private String iterationCreatedByName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime iterationUpdatedAt;
    private String iterationUpdatedBy;
    private String iterationUpdatedByName;
    private CostingInput costingInput;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime scenarioAssignedAt;
    private String scenarioAssignedBy;
    private String scenarioAssignedByName;
    private String scenarioAssignedTo;
    private String scenarioAssignedToName;
    private String scenarioCostMaturity;
    private List<Object> scenarioCustomAttributes;
    private String scenarioDescription;
    private List<Object> scenarioDesignInvestigations;
    private List<Object> scenarioDesignIssues;
    private List<Object> scenarioDesignNotices;
    private ScenarioMetadata scenarioMetadata;
    private List<Object> scenarioProcesses;
    private String scenarioStatus;
    private Integer scenarioSystemLockCode;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private String scenarioSystemLockedAt;
    private String scenarioUpdatedBy;
    private String scenarioUpdatedByName;
    private AnalysisOfChildren analysisOfChildren;
    private AnalysisOfScenario analysisOfScenario;
    private AnalysisOfScenarioAndChildren analysisOfScenarioAndChildren;
    private PartNestingDiagram partNestingDiagram;
    private Thumbnail thumbnail;
    private Boolean iterationHasWatchpointReport;

    public static class PartNestingDiagram {
    }


    public static class ScenarioMetadata {
    }
}