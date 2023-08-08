package com.apriori.qms.models.response.scenariodiscussion;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UsersResponseSchema.json")
public class ParticipantsResponse extends Pagination {
    List<Assignee> items;
}
