package com.apriori.dds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "CommentsResponseSchema.json")
public class CommentsResponse extends Pagination {
    public ArrayList<CommentResponse> items;
}
