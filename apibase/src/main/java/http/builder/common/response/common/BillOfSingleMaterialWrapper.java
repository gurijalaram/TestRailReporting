package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

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
