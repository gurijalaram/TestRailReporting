package com.apriori.entity.response;

import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX;
import com.apriori.utils.http.enums.Schema;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "DeploymentSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("response")
public class Deployment {
    private Boolean isDefault;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSXXX.class)
    private LocalDateTime updatedAt;
    private String customerIdentity;
    private String name;
    private String cloudReference;
    private String description;
    private String customerType;
    private String salesforceId;
    private Boolean active;
    private String apVersion;
    private List<Installation> installations = null;
    private List<Object> siteIdentities = null;
    private String deploymentType;
    private Integer maxCadFileRetentionDays;
    private Integer maxCadFileSize;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private List<String> oneTimePasswordApplications;
    private String createdByName;
    private List<String> identityProviders;
    private String authenticationType;
    private List<String> emailDomains;
}
