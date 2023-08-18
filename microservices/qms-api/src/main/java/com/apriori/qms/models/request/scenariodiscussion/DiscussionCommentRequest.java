package com.apriori.qms.models.request.scenariodiscussion;

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
