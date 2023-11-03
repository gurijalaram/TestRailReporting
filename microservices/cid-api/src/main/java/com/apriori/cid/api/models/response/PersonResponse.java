package com.apriori.cid.api.models.response;

import com.apriori.shared.util.annotations.Schema;

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
    private String prefix;
    private String suffix;
}
