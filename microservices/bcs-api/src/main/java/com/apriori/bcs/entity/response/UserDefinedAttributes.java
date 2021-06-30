package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("response")
@Schema(location = "UserDefinedAttributesSchema.json")
public class UserDefinedAttributes extends Pagination {
    private String identity;
    private String customerIdentity;
    private Integer ordinal;
    private String name;
    private String displayName;
    private String type;
    private String requiredAttributeType;
    private String multiSelect;
    private String[] allowedValues;
    private List<UserDefinedAttributes> items;
    private String defaultValue;
    private Integer precision;
}
