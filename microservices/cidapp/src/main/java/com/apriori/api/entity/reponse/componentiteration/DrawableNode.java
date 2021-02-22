package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrawableNode {
    private DrawableAdditionalProp1 additionalProp1;
    private DrawableAdditionalProp2 additionalProp2;
    private DrawableAdditionalProp3 additionalProp3;

    public DrawableAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    public DrawableNode setAdditionalProp1(DrawableAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
        return this;
    }

    public DrawableAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    public DrawableNode setAdditionalProp2(DrawableAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
        return this;
    }

    public DrawableAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    public DrawableNode setAdditionalProp3(DrawableAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
        return this;
    }
}
