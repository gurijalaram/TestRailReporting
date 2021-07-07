package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("response")
@Schema(location = "CustomAttributesSchema.json")
public class CustomAttributes extends Pagination {
    private String[] allowedValues;
    private Integer ordinal;
    private String identity;
    private String customerIdentity;
    private String displayName;
    private Boolean multiSelect;
    private String name;
    private String requiredAttributeType;
    private String type;
    private CustomAttributes[] items;
    private String defaultValue;
    private Integer precision;

}