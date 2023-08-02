package com.apriori.cidappapi.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "PeopleSchema.json")
@JsonRootName("response")
@Data
public class PeopleResponse extends Pagination {
    List<PersonResponse> items;
}
