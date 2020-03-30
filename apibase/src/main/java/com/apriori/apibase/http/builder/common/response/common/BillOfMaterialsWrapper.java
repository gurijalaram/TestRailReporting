package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Schema(location = "BillOfMaterialsSchema.json")
public class BillOfMaterialsWrapper extends BasePageResponse {

    @JsonProperty("items")
    private List<BillOfMaterial> billOfMaterialsList;

    public List<BillOfMaterial> getBillOfMaterialsList() {
        return billOfMaterialsList;
    }

    public BillOfMaterialsWrapper setBillOfMaterialsList(List<BillOfMaterial> billOfMaterialsList) {
        this.billOfMaterialsList = billOfMaterialsList;
        return this;
    }
}
