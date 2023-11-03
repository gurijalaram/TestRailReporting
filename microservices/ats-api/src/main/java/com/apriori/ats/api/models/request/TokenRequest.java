package com.apriori.ats.api.models.request;

import com.apriori.ats.api.models.response.TokenInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

