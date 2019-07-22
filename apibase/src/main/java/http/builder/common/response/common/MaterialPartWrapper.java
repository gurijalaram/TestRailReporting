package main.java.http.builder.common.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.http.enums.Schema;

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
