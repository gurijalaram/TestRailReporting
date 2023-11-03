package com.apriori.qms.api.models.response.scenariodiscussion;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DiscussionCommentResponseSchema.json")
public class DiscussionCommentResponse {
    private String identity;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String content;
    private String discussionIdentity;
    private String status;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime publishedAt;
    private ArrayList<CommentView> commentView;
    private ArrayList<MentionedUser> mentionedUsers;
    private String creatorAvatarColor;
    private String customerIdentity;
}
