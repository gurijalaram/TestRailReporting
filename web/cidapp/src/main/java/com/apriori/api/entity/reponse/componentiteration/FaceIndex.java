package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "additionalProp1",
    "additionalProp2",
    "additionalProp3"
})
public class FaceIndex {

    @JsonProperty("additionalProp1")
    private FaceAdditionalProp1 additionalProp1;
    @JsonProperty("additionalProp2")
    private FaceAdditionalProp2 additionalProp2;
    @JsonProperty("additionalProp3")
    private FaceAdditionalProp3 additionalProp3;

    @JsonProperty("additionalProp1")
    public FaceAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public FaceIndex setAdditionalProp1(FaceAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
        return this;
    }

    @JsonProperty("additionalProp2")
    public FaceAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public FaceIndex setAdditionalProp2(FaceAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
        return this;
    }

    @JsonProperty("additionalProp3")
    public FaceAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public FaceIndex setAdditionalProp3(FaceAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
        return this;
    }
}
