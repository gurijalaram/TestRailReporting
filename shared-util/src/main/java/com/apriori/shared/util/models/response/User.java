package com.apriori.shared.util.models.response;

import com.apriori.serialization.util.deserializers.DateTimeDeserializer_yyyyMMddTHHmmssSSSZ;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.annotations.CreatableModel;
import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "UserSchema.json")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@CreatableModel("user")
@JsonRootName("response")
public class User {
    @Expose
    protected Boolean isSystemUser;
    @Expose
    protected String identity;
    @Expose
    protected String userType;
    @Expose
    protected UserProfile userProfile;
    @Expose
    protected String email;
    @Expose
    protected String username;
    @Expose
    protected Boolean active;
    @Expose
    protected List<UserSite> sites;
    @Expose
    protected CustomAttributes customAttributes;
    @Expose
    protected List<Roles> roles;
    @Expose
    protected String customerIdentity;
    @Expose
    protected Boolean mfaRequired;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    protected LocalDateTime createdAt;
    protected String createdBy;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    protected LocalDateTime updatedAt;
    @Expose
    protected String updatedBy;
    @Expose
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer_yyyyMMddTHHmmssSSSZ.class)
    protected LocalDateTime deletedAt;
    @Expose
    protected String deletedBy;
    @Expose
    protected Boolean hasCompleteProfile;
    @Expose
    protected Enablements enablements;
    @Expose
    protected String mfaAuthenticator;
    @Expose
    protected List<Object> resourcesAllowedToCreate;
    @Expose
    protected CustomProperties customProperties;
    @Expose
    protected String createdByName;
    @Expose
    protected String updatedByName;
    @Expose
    protected List<Object> licenseAssignments;
    @Expose
    protected String awsRole;
    @Expose
    protected Boolean canDelete;
    @Expose
    protected Boolean canEditEnablements;
    @Expose
    protected Boolean canEditProfile;
    private Customer customerData;

    public Customer getCustomerData() {
        return customerData = customerData == null ? SharedCustomerUtil.getCustomerData() : customerData;
    }
}
