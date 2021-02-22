package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeEntry {
    private NodeAdditionalProp1 additionalProp1;
    private NodeAdditionalProp2 additionalProp2;
    private NodeAdditionalProp3 additionalProp3;

    public NodeAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    public NodeEntry setAdditionalProp1(NodeAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
        return this;
    }

    public NodeAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    public NodeEntry setAdditionalProp2(NodeAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
        return this;
    }

    public NodeAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    public NodeEntry setAdditionalProp3(NodeAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
        return this;
    }
}
