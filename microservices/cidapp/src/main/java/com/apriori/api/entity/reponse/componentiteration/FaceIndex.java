package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaceIndex {
    private FaceAdditionalProp1 additionalProp1;
    private FaceAdditionalProp2 additionalProp2;
    private FaceAdditionalProp3 additionalProp3;

    public FaceAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    public FaceIndex setAdditionalProp1(FaceAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
        return this;
    }

    public FaceAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    public FaceIndex setAdditionalProp2(FaceAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
        return this;
    }

    public FaceAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    public FaceIndex setAdditionalProp3(FaceAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
        return this;
    }
}
