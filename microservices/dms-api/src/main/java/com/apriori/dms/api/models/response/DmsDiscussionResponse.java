package com.apriori.dms.api.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DmsDiscussionResponseSchema.json")
public class DmsDiscussionResponse {
    private String identity;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String description;
    private String status;
    private ArrayList<DmsCommentResponse> comments;
    private ArrayList<DmsParticipant> participants;
    private String updatedBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime assignedAt;
    private DmsAssignee assignee;
    private DmsAttributes attributes;
}
