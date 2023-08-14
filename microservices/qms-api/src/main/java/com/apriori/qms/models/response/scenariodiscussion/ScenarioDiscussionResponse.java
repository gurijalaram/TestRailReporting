package com.apriori.qms.models.response.scenariodiscussion;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "ScenarioDiscussionResponseSchema.json")
public class ScenarioDiscussionResponse {
    public String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    public String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    public LocalDateTime updatedAt;
    public String updatedBy;
    public String assigneeUserIdentity;
    public Assignee assignee;
    public Attributes attributes;
    public String customerIdentity;
    public String description;
    public String type;
    public ArrayList<Comment> comments;
    public ArrayList<Participant> participants;
    public String status;
    public String componentIdentity;
    public String scenarioIdentity;
    public String projectIdentity;
    public String projectItemIdentity;
}
