
package com.apriori.ats.entity.response;

import com.apriori.ats.entity.request.Claims;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenInformation {

    @JsonProperty
    private String issuer;

    @JsonProperty
    private String subject;

    @JsonProperty
    private Claims claims;

    public TokenInformation setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public TokenInformation setSubject(String subject) {
        this.subject =  subject;
        return this;
    }

    public String getSubject() {
        return this.subject;
    }

    public TokenInformation setNameAndEmail(String name, String email) {
        this.claims = new Claims();
        this.claims.setName(name);
        this.claims.setEmail(email);
        return this;
    }
}
