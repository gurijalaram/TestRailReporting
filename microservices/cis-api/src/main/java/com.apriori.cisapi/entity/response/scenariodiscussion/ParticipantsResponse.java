package com.apriori.cisapi.entity.response.scenariodiscussion;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UsersResponseSchema.json")
public class ParticipantsResponse extends Pagination {
    private List<Assignee> items;
}
