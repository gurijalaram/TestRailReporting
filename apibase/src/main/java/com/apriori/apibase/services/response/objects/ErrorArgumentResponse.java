package com.apriori.apibase.services.response.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorArgumentResponse {
    public ArrayList<String> codes;
    public Object arguments;
    public String defaultMessage;
    public String code;
}