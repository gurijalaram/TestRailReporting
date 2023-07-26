package com.apriori.authorization.request;

import com.apriori.authorization.response.TokenInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

