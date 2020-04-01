package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "BillOfSingleMaterialWrapperSchema.json")
public class BillOfSingleMaterialWrapper {

    @JsonProperty("response")
    private BillOfMaterial billOfMaterial;

    public BillOfMaterial getBillOfMaterial() {
        return billOfMaterial;
    }

    public BillOfSingleMaterialWrapper setBillOfMaterial(BillOfMaterial billOfMaterial) {
        this.billOfMaterial = billOfMaterial;
        return this;
    }
}
