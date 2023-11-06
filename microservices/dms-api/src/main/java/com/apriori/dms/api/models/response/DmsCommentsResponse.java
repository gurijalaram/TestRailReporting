package com.apriori.dms.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonRootName("response")
@Schema(location = "DmsCommentsResponseSchema.json")
public class DmsCommentsResponse extends Pagination {
    public ArrayList<DmsCommentResponse> items;
}
