package com.apriori.shared.util.models.request;

import com.apriori.shared.util.models.response.TokenInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

