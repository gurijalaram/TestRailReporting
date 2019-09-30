package com.apriori.apibase.http.builder.common.response.common;

import com.apriori.apibase.http.enums.Schema;

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
