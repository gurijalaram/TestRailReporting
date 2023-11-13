
package com.apriori.cds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CredentialsSchema.json")
@Data
@JsonRootName("response")
public class CredentialsItems {
    private String identity;
    private String userIdentity;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private List<String> passwordHashHistory = null;
    private List<String> passwordSaltHistory = null;
}