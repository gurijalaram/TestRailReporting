package com.apriori.ats.api.models.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthorizeRequest {
    private String targetCloudContext;
    private String token;
}
