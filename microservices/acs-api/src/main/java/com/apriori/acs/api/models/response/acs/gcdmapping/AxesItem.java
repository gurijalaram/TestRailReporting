package com.apriori.acs.api.models.response.acs.gcdmapping;

import lombok.Data;

import java.util.List;

@Data
public class AxesItem {
    private List<Double> origin;
    private List<Double> direction;
    private Double length;
}
