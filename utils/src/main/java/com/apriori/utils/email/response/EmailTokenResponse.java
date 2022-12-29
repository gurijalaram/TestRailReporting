package com.apriori.utils.email.response;

import com.apriori.utils.http.enums.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(location = "EmailTokenSchema.json")
public class EmailTokenResponse {
    private String token_type;
    private String scope;
    private Integer expires_in;
    private Integer ext_expires_in;
    private String access_token;
}