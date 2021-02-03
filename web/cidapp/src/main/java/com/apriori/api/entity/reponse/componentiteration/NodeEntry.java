package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeEntry {

    @JsonProperty("additionalProp1")
    private AdditionalProp1____ additionalProp1;
    @JsonProperty("additionalProp2")
    private AdditionalProp2____ additionalProp2;
    @JsonProperty("additionalProp3")
    private AdditionalProp3____ additionalProp3;

    @JsonProperty("additionalProp1")
    public AdditionalProp1____ getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public void setAdditionalProp1(AdditionalProp1____ additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    @JsonProperty("additionalProp2")
    public AdditionalProp2____ getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public void setAdditionalProp2(AdditionalProp2____ additionalProp2) {
        this.additionalProp2 = additionalProp2;
    }

    @JsonProperty("additionalProp3")
    public AdditionalProp3____ getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public void setAdditionalProp3(AdditionalProp3____ additionalProp3) {
        this.additionalProp3 = additionalProp3;
    }
}
