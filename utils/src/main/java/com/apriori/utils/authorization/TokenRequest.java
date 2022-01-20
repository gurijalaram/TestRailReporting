package com.apriori.utils.authorization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
    private TokenInformation token;
}

