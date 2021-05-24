package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "CustomAttributesSchema.json")
public class CustomAttributes extends Pagination {
    private String allowedValues;
    private Integer decimalPlaces;
    private String defaultValue;
    private String displayName;
    private Boolean multiselect;
    private String name;
    private Boolean required;
    private String type;
    private CustomAttributes[] items;

}