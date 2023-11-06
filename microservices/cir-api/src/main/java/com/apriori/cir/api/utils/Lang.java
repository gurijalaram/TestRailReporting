package com.apriori.cir.api.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Lang {
    public String decimalPoint;
    public String thousandsSep;
    public ArrayList<String> months;
    public ArrayList<String> shortMonths;
    public ArrayList<String> weekdays;
}
