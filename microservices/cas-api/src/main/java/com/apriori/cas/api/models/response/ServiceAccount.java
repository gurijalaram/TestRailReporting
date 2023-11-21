package com.apriori.cas.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ServiceAccountSchema.json")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccount extends User {
    private String serviceAccount;
    private String status;
}