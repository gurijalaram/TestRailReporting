package com.apriori.ats.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Schema(location = "common/AtsAuthorizeSchema.json")
public class AuthorizationResponse {
    @JsonProperty
    private Object response;

    @JsonProperty
    private String identity;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    private LocalDateTime createdAt;

    @JsonProperty
    private LocalDateTime updatedAt;

    @JsonProperty
    private String customerIdentity;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String username;

    @JsonProperty
    private Object[] userDeployments;
}
