package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.cidappapi.models.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.models.response.componentiteration.ScenarioCustomAttribute;
import com.apriori.cidappapi.models.response.componentiteration.ScenarioMetadata;
import com.apriori.cidappapi.models.response.componentiteration.ScenarioRoutings;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.models.response.CostingInput;
import com.apriori.models.response.ScenarioGcd;
import com.apriori.models.response.Thumbnail;
import com.apriori.models.response.componentiteration.PartNestingDiagram;
import com.apriori.models.response.componentiteration.ScenarioDesignInvestigations;
import com.apriori.models.response.componentiteration.ScenarioDesignIssues;
import com.apriori.models.response.componentiteration.ScenarioDesignNotices;
import com.apriori.models.response.componentiteration.ScenarioProcess;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "ScenarioIterationResponse.json")
@Data
@JsonRootName(value = "response")
public class ScenarioIteration {
    private ScenarioMetadata scenarioMetadata;
    private String customerIdentity;
    private String scenarioKey;
    private Object scenarioIterationKey;
    private String hasThumbnail;
    private Thumbnail thumbnail;
    private String costingMessage;
    private String createdBy;
    private String updatedBy;
    private CostingInput costingInput;
    private String identity;
    private ScenarioGcd scenarioGcd;
    private String iteration;
    private String hasWebImage;
    private Boolean hasWatchpointReport;
    private String hasCustomImage;
    private Object scenarioDtcIssues;
    private Boolean isCadConnected;
    private List<ScenarioCustomAttribute> scenarioCustomAttributes;
    private List<ScenarioDesignInvestigations> scenarioDesignInvestigations;
    private List<ScenarioDesignIssues> scenarioDesignIssues;
    private List<ScenarioDesignNotices> scenarioDesignNotices;
    private List<ScenarioRoutings> scenarioRoutings;
    private List<ScenarioProcess> scenarioProcesses;
    private AnalysisOfScenario analysisOfScenario;
    private PartNestingDiagram partNestingDiagram;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
