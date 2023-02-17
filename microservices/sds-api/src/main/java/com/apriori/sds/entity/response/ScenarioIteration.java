package com.apriori.sds.entity.response;

import com.apriori.entity.response.Thumbnail;
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
    private String customerIdentity;
    private String scenarioKey;
    private Object scenarioIterationKey;
    private String hasThumbnail;
    private Thumbnail thumbnail;
    private String costingMessage;
    private String createdBy;
    private String updatedBy;
    private String identity;
    private String iteration;
    private String hasWebImage;
    private Boolean hasWatchpointReport;
    private String hasCustomImage;
    private Object scenarioDtcIssues;
    private Boolean isCadConnected;
    private List<String> scenarioCustomAttributes;
    private List<String> scenarioDesignInvestigations;
    private List<String> scenarioDesignIssues;
    private List<String> scenarioDesignNotices;
    private List<String> scenarioRoutings;
    private List<String> scenarioProcesses;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
}
