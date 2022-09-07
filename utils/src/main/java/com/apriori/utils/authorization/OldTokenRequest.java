package com.apriori.utils.authorization;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Schema(location = "common/OldTokenSchema.json")
@Data
public class OldTokenRequest {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
}
