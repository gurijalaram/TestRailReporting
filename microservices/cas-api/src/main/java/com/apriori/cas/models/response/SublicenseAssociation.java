package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;
import com.apriori.models.response.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "UserSchema.json")
@Data
@JsonRootName("response")
public class SublicenseAssociation extends Pagination {
    private List<User> items;
}