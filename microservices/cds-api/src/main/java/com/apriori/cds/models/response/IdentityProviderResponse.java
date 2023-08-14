package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(location = "IdentityProviderSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonRootName("response")
public class IdentityProviderResponse {
    private String identity;
    private String createdBy;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private IdentityProviderPlatform identityProviderPlatform;
    private Boolean active;
    private String name;
    private String displayName;
    private String description;
    private List<String> idpDomains = null;
    private String signInUrl;
    private String signingCertificate;
    private String signingCertificateExpiresAt;
    private Boolean signRequest;
    private AttributeMappings attributeMappings;
    private String authenticationType;
    private String customerIdentity;
    private String signRequestAlgorithm;
    private String signRequestAlgorithmDigest;
    private String protocolBinding;
}
