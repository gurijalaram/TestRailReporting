package com.apriori.apibase.services.response.objects;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(location = "MaterialPartWrapperSchema.json")
public class MaterialPartWrapper {

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
