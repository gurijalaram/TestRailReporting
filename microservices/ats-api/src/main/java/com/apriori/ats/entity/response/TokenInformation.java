
package com.apriori.ats.entity.response;

import com.apriori.ats.entity.request.Claims;

import lombok.Data;

@Data
public class TokenInformation {
    private String issuer;
    private String subject;
    private Claims claims;
}
