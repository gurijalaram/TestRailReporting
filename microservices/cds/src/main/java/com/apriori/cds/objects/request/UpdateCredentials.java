package com.apriori.cds.objects.request;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "cds/UpdateCredentialsSchema.json")
public class UpdateCredentials {

    private String currentPasswordHash;
    private String newPasswordHash;
    private String newEncryptedPassword;
    private String newPasswordSalt;

    public String getCurrentPasswordHash() {
        return currentPasswordHash;
    }

    public UpdateCredentials setCurrentPasswordHash(String currentPasswordHash) {
        this.currentPasswordHash = currentPasswordHash;
        return this;
    }

    public String getNewPasswordHash() {
        return newPasswordHash;
    }

    public UpdateCredentials setNewPasswordHash(String newPasswordHash) {
        this.newPasswordHash = newPasswordHash;
        return this;
    }

    public String getNewEncryptedPassword() {
        return newEncryptedPassword;
    }

    public UpdateCredentials setNewEncryptedPassword(String newEncryptedPassword) {
        this.newEncryptedPassword = newEncryptedPassword;
        return this;
    }

    public String getNewPasswordSalt() {
        return newPasswordSalt;
    }

    public UpdateCredentials setNewPasswordSalt(String newPasswordSalt) {
        this.newPasswordSalt = newPasswordSalt;
        return this;
    }
}