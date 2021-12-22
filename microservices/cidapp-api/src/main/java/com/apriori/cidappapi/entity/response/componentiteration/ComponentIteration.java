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

    static class AnalysisOfScenarioAndChildren {
    }

    static class AnalysisOfChildren {
    }
}
