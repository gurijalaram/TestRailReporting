
package com.apriori.ats.api.models.response;

import com.apriori.ats.api.models.request.Claims;

import lombok.Data;

@Data
public class TokenInformation {
    private String issuer;
    private String subject;
    private Claims claims;
}
