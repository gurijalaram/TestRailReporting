package com.apriori.ats.models.request;

import com.apriori.ats.models.response.TokenInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

