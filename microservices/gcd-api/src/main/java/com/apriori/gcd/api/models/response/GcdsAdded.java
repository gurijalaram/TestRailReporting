package com.apriori.gcd.api.models.response;

import lombok.Data;

@Data
public class GcdsAdded {
    private String gcdName;
    private String gcdType;
    private Integer sequenceNumber;
}
