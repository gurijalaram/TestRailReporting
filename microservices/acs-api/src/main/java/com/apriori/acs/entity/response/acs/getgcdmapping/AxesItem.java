package com.apriori.acs.entity.response.acs.getgcdmapping;

import lombok.Data;

import java.util.List;

@Data
public class AxesItem {
    private List<Double> origin;
    private List<Double> direction;
    private Double length;
}