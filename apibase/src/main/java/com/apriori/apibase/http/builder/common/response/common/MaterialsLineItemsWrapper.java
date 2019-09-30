package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "MaterialsLineItemsWrapperSchema.json")
public class MaterialsLineItemsWrapper extends BasePageResponse {

    @JsonProperty("items")
    private List<MaterialLineItem> materialLineItems;

    public List<MaterialLineItem> getMaterialLineItems() {
        return materialLineItems;
    }

    public MaterialsLineItemsWrapper setMaterialLineItems(List<MaterialLineItem> materialLineItems) {
        this.materialLineItems = materialLineItems;
        return this;
    }
}
