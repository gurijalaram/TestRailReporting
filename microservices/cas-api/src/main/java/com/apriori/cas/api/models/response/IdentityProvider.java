package com.apriori.cas.api.models.response;

import com.apriori.cds.api.models.response.IdentityProviderPlatform;
import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "IdentityProviderCasSchema.json")
@JsonRootName("response")
@Data
public class IdentityProvider {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String customerIdentity;
    private Boolean active;
    private String name;
    private IdentityProviderPlatform identityProviderPlatform;
    private String displayName;
    private String description;
    private List<Object> idpDomains = null;
    private String signInUrl;
    private String signingCertificate;
    private String signingCertificateExpiresAt;
    private Boolean signRequest;
    private AttributeMappings attributeMappings;
    private String createdByName;
    private String signRequestAlgorithm;
    private String signRequestAlgorithmDigest;
    private String protocolBinding;
    private String authenticationType;
}
