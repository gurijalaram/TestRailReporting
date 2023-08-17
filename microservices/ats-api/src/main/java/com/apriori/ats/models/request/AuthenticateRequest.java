package com.apriori.ats.models.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticateRequest {
    private String email;
    private String password;
}