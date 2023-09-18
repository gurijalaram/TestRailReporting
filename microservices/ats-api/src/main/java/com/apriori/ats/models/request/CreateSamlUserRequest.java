package com.apriori.ats.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSamlUserRequest {
    private String email;
    private String given_name;
    private String family_name;
    private String name;
    private String roles;
}