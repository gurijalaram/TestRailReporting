package com.apriori.cidappapi.entity.response.componentiteration;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(location = "ComponentIterationResponse.json")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComponentIteration {
    private ComponentIteration response;
    private Boolean isCadConnected;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String createdByName;
    private String updatedByName;
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
    private List<ScenarioDesignInvestigations> scenarioDesignInvestigations;
    private List<ScenarioDesignIssues> scenarioDesignIssues;
    private List<ScenarioDesignNotices> scenarioDesignNotices;
    private Boolean hasCustomImage;

    @Data
    @Builder
    static class AnalysisOfScenarioAndChildren {
    }

    @Data
    @Builder
    static class AnalysisOfChildren {

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ScenarioDesignIssues {
        private String identity;
        private String attribute;
        private String attributeDisplayName;
        private String category;
        private String categoryDisplayName;
        private String issueDescription;
        private String issueSeverity;
        private IssueSpecificProperties issueSpecificProperties;
        private String primaryGcdArtifactType;
        private String primaryGcdDisplayName;
        private String primaryGcdSequenceNumber;
        private String processGroupName;
        private String processName;
        private String secondaryGcdArtifactType;
        private String secondaryGcdDisplayName;
        private Integer secondaryGcdSequenceNumber;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class IssueSpecificProperties {
        private String tableType;
        private String panelOutput;
        private String unitType;
        private Integer current;
        private Integer suggestedMin;
        private String edgeList;
        private OpName opName;
        private ProcName procName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class OpName {
        private String processGroupName;
        private String processName;
        private Integer index;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ProcName {
        private String processGroupName;
        private String processName;
        private Integer index;
        private String displayName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class ScenarioDesignNotices {
        private String identity;
        private String attribute;
        private String attributeDisplayName;
        private String category;
        private String categoryDisplayName;
        private String gcdArtifactType;
        private String gcdDisplayName;
        private Integer gcdSequenceNumber;
        private NoticeSpecificProperties noticeSpecificProperties;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class NoticeSpecificProperties {
        private String operationSequence;
        private Integer current;
        private Integer threadLength;
        private Integer defaultThreadLength;
        private Integer cadThreadLength;
        private Integer suggested;
        private Integer cycleTime;
        private Integer cycleTimePercent;
        private String guidanceTopic;
        private String processGroup;
        private Boolean threaded;
        private Boolean cadThreaded;
    }
}