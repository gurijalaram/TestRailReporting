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
public class DrawableNode {

    @JsonProperty("additionalProp1")
    private DrawableAdditionalProp1 additionalProp1;
    @JsonProperty("additionalProp2")
    private DrawableAdditionalProp2 additionalProp2;
    @JsonProperty("additionalProp3")
    private DrawableAdditionalProp3 additionalProp3;

    @JsonProperty("additionalProp1")
    public DrawableAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public void setAdditionalProp1(DrawableAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    @JsonProperty("additionalProp2")
    public DrawableAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public void setAdditionalProp2(DrawableAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
    }

    @JsonProperty("additionalProp3")
    public DrawableAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public void setAdditionalProp3(DrawableAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
    }
}
