package com.apriori.shared.util.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(location = "EmailTokenSchema.json")
public class EmailTokenResponse {
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("ext_expires_in")
    private Integer extExpiresIn;
    @JsonProperty("access_token")
    private String accessToken;
}