package com.apriori.ats.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Schema(location = "CloudContextSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
public class CloudContextResponse {
    private String customerIdentity;
    private String deploymentIdentity;
    private String installationIdentity;
    private String applicationIdentity;
}