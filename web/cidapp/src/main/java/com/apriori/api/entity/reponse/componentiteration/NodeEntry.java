package com.apriori.api.entity.reponse.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeEntry {

    @JsonProperty("additionalProp1")
    private NodeAdditionalProp1 additionalProp1;
    @JsonProperty("additionalProp2")
    private NodeAdditionalProp2 additionalProp2;
    @JsonProperty("additionalProp3")
    private NodeAdditionalProp3 additionalProp3;

    @JsonProperty("additionalProp1")
    public NodeAdditionalProp1 getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public void setAdditionalProp1(NodeAdditionalProp1 additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    @JsonProperty("additionalProp2")
    public NodeAdditionalProp2 getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public void setAdditionalProp2(NodeAdditionalProp2 additionalProp2) {
        this.additionalProp2 = additionalProp2;
    }

    @JsonProperty("additionalProp3")
    public NodeAdditionalProp3 getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public void setAdditionalProp3(NodeAdditionalProp3 additionalProp3) {
        this.additionalProp3 = additionalProp3;
    }
}
