package com.apriori.cis.models.response.scenariodiscussion;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UsersResponseSchema.json")
public class ParticipantsResponse extends Pagination {
    private List<Assignee> items;
}
