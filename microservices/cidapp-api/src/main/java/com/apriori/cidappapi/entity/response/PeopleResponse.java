package com.apriori.cidappapi.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

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
