package com.apriori.cds.objects.request;

import com.apriori.utils.http.enums.Schema;

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