package com.apriori.qms.entity.request.scenariodiscussion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionCommentRequest {
    private DiscussionCommentParameters comment;
}
