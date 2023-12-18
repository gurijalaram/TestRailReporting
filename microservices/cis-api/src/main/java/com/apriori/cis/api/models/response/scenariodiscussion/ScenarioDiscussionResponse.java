package com.apriori.cis.api.models.response.scenariodiscussion;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.User;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionResponseSchema.json")
public class ScenarioDiscussionResponse {
    private String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String createdByName;
    private String updatedByName;
    private String assigneeUserIdentity;
    private User assignee;
    private Attributes attributes;
    private String customerIdentity;
    private String description;
    private String type;
    private List<Comment> comments;
    private List<Participant> participants;
    private String status;
    private String componentIdentity;
    private String scenarioIdentity;
    private String projectIdentity;
    private String projectItemIdentity;
}
