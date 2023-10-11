package com.apriori.cis.models.response.scenariodiscussion;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;
import com.apriori.models.response.User;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UsersSchema.json")
public class ParticipantsResponse extends Pagination {
    private List<User> items;
}
