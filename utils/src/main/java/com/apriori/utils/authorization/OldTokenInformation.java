package com.apriori.utils.authorization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OldTokenInformation {
    private String grantType;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String scope;
}
