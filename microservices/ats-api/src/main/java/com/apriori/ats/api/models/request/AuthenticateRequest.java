package com.apriori.ats.api.models.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticateRequest {
    private String email;
    private String password;
}