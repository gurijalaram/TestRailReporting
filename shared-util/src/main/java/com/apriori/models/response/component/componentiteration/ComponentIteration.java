package com.apriori.models.response.component.componentiteration;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

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
@Schema(location = "ComponentIterationResponse.json")
@JsonRootName("response")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComponentIteration {
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
    private String costingMessage;
    private String scenarioIterationKey;
    private CostingInput costingInput;
    private List<ScenarioCustomAttribute> scenarioCustomAttributes = null;
    private List<ScenarioDtcIssue> scenarioDtcIssues = null;
    private ScenarioGcd scenarioGcd;
    private ScenarioMetadata scenarioMetadata;
    private List<ScenarioProcess> scenarioProcesses = null;
    private List<ScenarioRoutings> scenarioRoutings;
    private AnalysisOfChildren analysisOfChildren;
    private AnalysisOfScenario analysisOfScenario;
    private AnalysisOfScenarioAndChildren analysisOfScenarioAndChildren;
    private PartNestingDiagram partNestingDiagram;
    private Boolean hasThumbnail;
    private Boolean hasWatchpointReport;
    private Boolean hasWebImage;
    private Thumbnail thumbnail;
    private Material material;
    private MaterialStock materialStock;
    private List<ScenarioDesignInvestigations> scenarioDesignInvestigations;
    private List<ScenarioDesignIssues> scenarioDesignIssues;
    private List<ScenarioDesignNotices> scenarioDesignNotices;
    private Boolean hasCustomImage;
}