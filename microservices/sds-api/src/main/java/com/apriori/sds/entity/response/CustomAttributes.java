package com.apriori.sds.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomAttributes {
    @JsonProperty("StringPredefDefault_1")
    private List<String> stringPredefDefault1;
    @JsonProperty("UDARegion")
    private String udaRegion;
    @JsonProperty("UserListMulti")
    private List<String> userListMulti;
    @JsonProperty("UserList")
    private String userList;
    @JsonProperty("BoxMaterial")
    private String boxMaterial;
    @JsonProperty("ShippingCompany")
    private String shippingCompany;
}
