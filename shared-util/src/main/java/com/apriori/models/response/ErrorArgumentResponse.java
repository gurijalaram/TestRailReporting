package com.apriori.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorArgumentResponse {
    public ArrayList<String> codes;
    public Object arguments;
    public String defaultMessage;
    public String code;
}