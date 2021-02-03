
package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AxesEntries {

    @JsonProperty("additionalProp1")
    private AxesAdditionalProp1 additionalProp1;
    @JsonProperty("additionalProp2")
    private AxesAdditionalProp2 additionalProp2;
    @JsonProperty("additionalProp3")
    private AxesAdditionalProp3 additionalProp3;

    @JsonProperty("additionalProp1")
    public AxesAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public void setAdditionalProp1(AxesAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    @JsonProperty("additionalProp2")
    public AxesAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public void setAdditionalProp2(AxesAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
    }

    @JsonProperty("additionalProp3")
    public AxesAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public void setAdditionalProp3(AxesAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
    }
}
