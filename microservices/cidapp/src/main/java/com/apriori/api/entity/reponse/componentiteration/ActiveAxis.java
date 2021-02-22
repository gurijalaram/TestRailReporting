package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActiveAxis {

    @JsonProperty("additionalProp1")
    private ActiveAdditionalProp1 additionalProp1;
    @JsonProperty("additionalProp2")
    private ActiveAdditionalProp2 additionalProp2;
    @JsonProperty("additionalProp3")
    private ActiveAdditionalProp3 additionalProp3;

    @JsonProperty("additionalProp1")
    public ActiveAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public ActiveAxis setAdditionalProp1(ActiveAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
        return this;
    }

    @JsonProperty("additionalProp2")
    public ActiveAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public ActiveAxis setAdditionalProp2(ActiveAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
        return this;
    }

    @JsonProperty("additionalProp3")
    public ActiveAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public ActiveAxis setAdditionalProp3(ActiveAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
        return this;
    }
}
