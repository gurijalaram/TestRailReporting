package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.builder.common.response.common.PayloadJSON;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "MaterialPartWrapperSchema.json")
public class MaterialPartWrapper implements PayloadJSON {

    @JsonProperty("response")
    private MaterialPart materialPart;

    public MaterialPart getMaterialPart() {
        return materialPart;
    }

    public MaterialPartWrapper setMaterialPart(MaterialPart materialPart) {
        this.materialPart = materialPart;
        return this;
    }
}
