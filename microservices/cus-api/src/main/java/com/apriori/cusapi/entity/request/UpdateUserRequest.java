package com.apriori.cusapi.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class UpdateUserRequest {
    private UserProfile userProfile;
    private String username;
    private String email;
    private String identity;
    private String createdBy;
    private Boolean active;
}
