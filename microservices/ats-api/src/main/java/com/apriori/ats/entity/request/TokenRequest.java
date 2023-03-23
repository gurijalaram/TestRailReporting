package com.apriori.ats.entity.request;

import com.apriori.ats.entity.response.TokenInformation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

