package com.apriori.ats.models.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizeRequest {
    private String targetCloudContext;
    private String token;
}
