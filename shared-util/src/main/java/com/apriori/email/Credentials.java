package com.apriori.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    private String tenantId;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String username;
    private String password;
    private String scope;
}