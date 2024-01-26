package com.apriori.dds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DdsCommentsResponseSchema.json")
public class CommentsResponse extends Pagination {
    public ArrayList<CommentResponse> items;
}
