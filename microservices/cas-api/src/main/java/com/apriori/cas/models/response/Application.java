package com.apriori.cas.models.response;

import com.apriori.annotations.Schema;
import com.apriori.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(location = "ApplicationSchema.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    private Boolean isSingleTenant;
    private Boolean isCloudHomeApp;
    private Boolean isPublic;
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String name;
    private String serviceName;
    private String cloudReference;
    private String description;
    private String customerType;
    private String salesforceId;
    private String active;
    private String iconUrl;
    private Integer maxCadFileRetentionDays;
    private Integer maxCadFileSize;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private List<String> oneTimePasswordApplications;
    private String createdByName;
    private List<String> identityProviders;
    private List<String> emailDomains;
    private String authenticationType;
    private List<String> roles;
}
