package com.apriori.ach.models.response;

import com.apriori.annotations.Schema;

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
    private String prefix;
    private String suffix;
}