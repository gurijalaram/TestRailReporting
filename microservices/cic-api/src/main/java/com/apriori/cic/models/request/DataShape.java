package com.apriori.cic.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataShape {
    public FieldDefinitions fieldDefinitions;
}
