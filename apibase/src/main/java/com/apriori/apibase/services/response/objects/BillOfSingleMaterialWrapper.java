package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
