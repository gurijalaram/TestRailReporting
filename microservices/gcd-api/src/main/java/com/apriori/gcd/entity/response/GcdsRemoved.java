package com.apriori.gcd.entity.response;

import lombok.Data;

@Data
public class GcdsRemoved {
    private String gcdName;
    private String gcdType;
    private Integer sequenceNumber;
}
