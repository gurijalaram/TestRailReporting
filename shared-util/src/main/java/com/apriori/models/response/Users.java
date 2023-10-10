package com.apriori.models.response;

import com.apriori.annotations.Schema;
import com.apriori.interfaces.Paged;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "UsersSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName("response")
public class Users extends Pagination implements Paged<User> {
    private List<User> items;
}

