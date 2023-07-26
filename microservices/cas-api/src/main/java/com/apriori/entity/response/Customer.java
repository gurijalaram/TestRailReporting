package com.apriori.entity.response;

import com.apriori.annotations.CreatableModel;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("response")
@Data
@CreatableModel("customer")
@Builder
public class Customer {
    private String identity;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String name;
    private String cloudReference;
    private String description;
    private String customerType;
    private String salesforceId;
    private Boolean active;
    private Integer maxCadFileRetentionDays;
    private Integer maxCadFileSize;
    private String mfaAuthenticator = null;
    private Boolean useExternalIdentityProvider;
    private Boolean mfaRequired;
    private List<Object> oneTimePasswordApplications = null;
    private String createdByName;
    private String updatedByName;
    private List<Object> identityProviders = null;
    private List<String> emailDomains = null;
    private String authenticationType;
}