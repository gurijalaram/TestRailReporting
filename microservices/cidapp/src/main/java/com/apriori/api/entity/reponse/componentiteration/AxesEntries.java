package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxesEntries {
    private AxesAdditionalProp1 additionalProp1;
    private AxesAdditionalProp2 additionalProp2;
    private AxesAdditionalProp3 additionalProp3;

    public AxesAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    public AxesEntries setAdditionalProp1(AxesAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
        return this;
    }

    public AxesAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    public AxesEntries setAdditionalProp2(AxesAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
        return this;
    }

    public AxesAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    public AxesEntries setAdditionalProp3(AxesAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
        return this;
    }
}
