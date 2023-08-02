package com.apriori.dms.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DmsCommentsResponseSchema.json")
public class DmsCommentsResponse extends Pagination {
    public ArrayList<DmsCommentResponse> items;
}
