package com.apriori.cds.objects.response;

import com.apriori.annotations.Schema;
import com.apriori.interfaces.Paged;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(location = "AuthUsersSchema.json")
@JsonRootName("response")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users extends Pagination implements Paged<User> {
    private List<User> items;
}

