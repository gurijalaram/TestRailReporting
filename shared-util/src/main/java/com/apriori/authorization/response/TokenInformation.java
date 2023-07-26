
package com.apriori.authorization.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import lombok.Data;

@JsonRootName("token")
@Data
@Builder
public class TokenInformation {
    private String issuer;
    private String subject;
    private Claims claims;
}
