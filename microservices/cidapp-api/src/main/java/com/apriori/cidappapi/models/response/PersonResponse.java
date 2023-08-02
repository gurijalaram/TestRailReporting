package com.apriori.cidappapi.models.response;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "PersonSchema.json")
@Data
public class PersonResponse {
    private String identity;
    private String customerIdentity;
    private String email;
    private String username;
    private String givenName;
    private String familyName;
}
