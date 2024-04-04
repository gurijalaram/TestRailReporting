package com.apriori.cis.api.models.response.scenariodiscussion;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "DiscussionCommentsBulkResponseSchema.json")
public class DiscussionCommentsBulkResponse extends ArrayList<DiscussionCommentResponse> {
}
