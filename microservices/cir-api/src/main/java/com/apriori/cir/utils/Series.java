package com.apriori.cir.utils;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

@Data
public class Series {
    private String name;
    private String _jrid;
    private ArrayList<Property> properties;
    private ArrayList<Object> data;
}
