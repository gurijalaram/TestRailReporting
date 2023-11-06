package com.apriori.qms.api.models.request.scenariodiscussion;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionCommentParameters {
    private String status;
    private String content;
    private List<String> mentionedUserEmails;
}
