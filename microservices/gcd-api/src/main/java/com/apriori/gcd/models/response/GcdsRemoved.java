package com.apriori.gcd.models.response;

import lombok.Data;

@Data
public class GcdsRemoved {
    private String gcdName;
    private String gcdType;
    private Integer sequenceNumber;
}