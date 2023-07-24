package com.apriori.sds.entity.response;

import com.apriori.cidappapi.entity.response.componentiteration.AnalysisOfScenario;
import com.apriori.cidappapi.entity.response.componentiteration.ScenarioCustomAttribute;
import com.apriori.cidappapi.entity.response.componentiteration.ScenarioMetadata;
import com.apriori.cidappapi.entity.response.componentiteration.ScenarioRoutings;
import com.apriori.entity.response.CostingInput;
import com.apriori.entity.response.ScenarioGcd;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.entity.response.Thumbnail;
import com.apriori.entity.response.componentiteration.PartNestingDiagram;
import com.apriori.entity.response.componentiteration.ScenarioDesignInvestigations;
import com.apriori.entity.response.componentiteration.ScenarioDesignIssues;
import com.apriori.entity.response.componentiteration.ScenarioDesignNotices;
import com.apriori.entity.response.componentiteration.ScenarioProcess;
import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

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
