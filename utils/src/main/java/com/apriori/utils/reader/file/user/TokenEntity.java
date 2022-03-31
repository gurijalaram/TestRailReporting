package com.apriori.utils.reader.file.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    @JsonProperty("aud")
    private String audience;
    @JsonProperty("sub")
    private String subject;
    @JsonProperty("iss")
    private String issuer;
    private String name;
    @JsonProperty("exp")
    private String expireAt;
    @JsonProperty("iat")
    private String issuedAt;
    private String email;
}
