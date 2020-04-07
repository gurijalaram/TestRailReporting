package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

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
