package com.apriori.authorization.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CloudContext {
    private String customerIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String applicationIdentity;
}