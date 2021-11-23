package com.apriori.cidappapi.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "PersonSchema.json")
@Data
public class PersonResponse {
    private String identity;
    private String username;
    private String givenName;
    private String familyName;
}
