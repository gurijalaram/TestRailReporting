package com.apriori.qms.entity.response.scenariodiscussion;

import com.apriori.utils.http.enums.Schema;
import com.apriori.utils.json.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "DiscussionCommentViewResponse.json")
public class DiscussionCommentViewResponse {

    private List<CommentView> commentView;
    private String content;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private String createdAt;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private String discussionIdentity;
    private String identity;
    private List<Object> mentionedUsers;
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private String publishedAt;
    private String status;

}