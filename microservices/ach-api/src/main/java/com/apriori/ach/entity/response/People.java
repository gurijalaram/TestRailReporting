package com.apriori.ach.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "PeopleSchema.json")
@Data
@JsonRootName("response")
public class People {
    private String identity;
    private String customerIdentity;
    private String email;
    private String username;
    private String givenName;
    private String familyName;
}