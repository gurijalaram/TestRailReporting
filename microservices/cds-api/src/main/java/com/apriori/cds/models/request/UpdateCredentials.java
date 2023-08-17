package com.apriori.cds.models.request;

import com.apriori.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "UpdateCredentialsSchema.json")
@Data
@Builder
public class UpdateCredentials {
    private String currentPasswordHash;
    private String newPasswordHash;
    private String newPasswordSalt;
    private String newEncryptedPassword;
}