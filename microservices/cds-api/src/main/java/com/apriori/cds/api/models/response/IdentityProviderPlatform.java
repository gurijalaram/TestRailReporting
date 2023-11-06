
package com.apriori.cds.api.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class IdentityProviderPlatform {
    private String identity;
    private String createdBy;
    private String createdAt;
    private String description;
    private String name;
    private String vendor;
    private String identityProviderProtocol;
}
