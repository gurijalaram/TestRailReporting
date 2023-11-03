package com.apriori.cas.api.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AttributeMappings {
    private String userId;
    private String name;
    private String givenName;
    private String familyName;
    private String email;
}
