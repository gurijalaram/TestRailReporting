package com.apriori.models.request;

import com.apriori.models.response.TokenInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

