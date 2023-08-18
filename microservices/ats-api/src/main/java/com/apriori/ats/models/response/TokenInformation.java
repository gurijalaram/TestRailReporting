
package com.apriori.ats.models.response;

import com.apriori.ats.models.request.Claims;

import lombok.Data;

@Data
public class TokenInformation {
    private String issuer;
    private String subject;
    private Claims claims;
}
