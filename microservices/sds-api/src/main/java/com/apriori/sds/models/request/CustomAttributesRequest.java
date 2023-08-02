package com.apriori.sds.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomAttributesRequest {
    @JsonProperty("StringPredefDefault_1")
    private String stringPredefDefault1;
    @JsonProperty("BoxMaterial")
    private String boxMaterial;
    @JsonProperty("ShippingCompany")
    private String shippingCompany;
}
