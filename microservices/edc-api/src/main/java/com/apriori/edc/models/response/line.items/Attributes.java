package com.apriori.edc.models.response.line.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Attributes {
    private Integer id;
    private Boolean isRequired;
    private String name;
    @JsonProperty("datatype")
    private String dataType;
    private String value;
}
