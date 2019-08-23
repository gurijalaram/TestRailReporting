package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

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
