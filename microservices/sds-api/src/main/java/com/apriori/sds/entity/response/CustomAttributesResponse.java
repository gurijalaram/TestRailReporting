package com.apriori.sds.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class CustomAttributesResponse {
    @JsonProperty("UDARegion")
    private String udaRegion;
    @JsonProperty("StringPredefDefault_1")
    private String stringPredefDefault1;
    @JsonProperty("BoxMaterial")
    private String boxMaterial;
    @JsonProperty("ShippingCompany")
    private String shippingCompany;
}

