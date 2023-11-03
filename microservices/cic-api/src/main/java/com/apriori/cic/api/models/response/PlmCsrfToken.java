package com.apriori.cic.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@Schema(location = "PlmCsrfTokenSchema.json")
public class PlmCsrfToken {
    @JsonProperty("@odata.context")
    private String context;
    @JsonProperty("NonceKey")
    private String nonceKey;
    @JsonProperty("NonceValue")
    private String nonceValue;
}