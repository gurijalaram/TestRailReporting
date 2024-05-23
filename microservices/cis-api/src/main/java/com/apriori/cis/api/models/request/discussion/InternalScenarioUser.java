package com.apriori.cis.api.models.request.discussion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternalScenarioUser {
    private String  customerIdentity;
    private String userIdentity;
    private String email;

}
