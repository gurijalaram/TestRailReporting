package com.apriori.apibase.services.bcs.objects;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import java.util.List;

@Schema(location = "CisUserDefinedAttributesSchema.json")
public class UserDefinedAttributes extends Pagination {
    private String name;
    private String displayName;
    private String type;
    private String required;
    private String multiselect;
    private String[] allowedValues;
    private Integer decimalPlaces;
    private String defaultValue;
    private UserDefinedAttributes response;
    private List<UserDefinedAttributes> items;

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public UserDefinedAttributes setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Integer getDecimalPlaces() {
        return this.decimalPlaces;
    }

    public UserDefinedAttributes setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        return this;
    }

    public UserDefinedAttributes getResponse() {
        return this.response;
    }

    public UserDefinedAttributes setResponse(UserDefinedAttributes response) {
        this.response = response;
        return this;
    }

    public List<UserDefinedAttributes> getItems() {
        return this.items;
    }

    public UserDefinedAttributes setItems(List<UserDefinedAttributes> items) {
        this.items = items;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public UserDefinedAttributes setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public UserDefinedAttributes setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public UserDefinedAttributes setType(String type) {
        this.type = type;
        return this;
    }

    public String getRequired() {
        return this.required;
    }

    public UserDefinedAttributes setRequired(String required) {
        this.required = required;
        return this;
    }

    public String getMultiselect() {
        return this.multiselect;
    }

    public UserDefinedAttributes setMultiselect(String multiselect) {
        this.multiselect = multiselect;
        return this;
    }

    public String[] getAllowedValues() {
        return this.allowedValues;
    }

    public UserDefinedAttributes setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
        return this;
    }
}
