package com.apriori.sds.api.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class CustomAttributesResponse {
    @JsonProperty("StringPredefDefault_1")
    private String stringPredefDefault1;
    @JsonProperty("BoxMaterial")
    private String boxMaterial;
    @JsonProperty("ShippingCompany")
    private String shippingCompany;
}

