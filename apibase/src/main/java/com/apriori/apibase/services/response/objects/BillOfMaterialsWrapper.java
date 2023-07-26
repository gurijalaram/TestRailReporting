package com.apriori.apibase.services.response.objects;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
