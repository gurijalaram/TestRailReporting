
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
public class AxesEntry {

    @JsonProperty("additionalProp1")
    private AdditionalProp1_ additionalProp1;
    @JsonProperty("additionalProp2")
    private AdditionalProp2_ additionalProp2;
    @JsonProperty("additionalProp3")
    private AdditionalProp3_ additionalProp3;

    @JsonProperty("additionalProp1")
    public AdditionalProp1_ getAdditionalProp1() {
        return additionalProp1;
    }

    @JsonProperty("additionalProp1")
    public void setAdditionalProp1(AdditionalProp1_ additionalProp1) {
        this.additionalProp1 = additionalProp1;
    }

    @JsonProperty("additionalProp2")
    public AdditionalProp2_ getAdditionalProp2() {
        return additionalProp2;
    }

    @JsonProperty("additionalProp2")
    public void setAdditionalProp2(AdditionalProp2_ additionalProp2) {
        this.additionalProp2 = additionalProp2;
    }

    @JsonProperty("additionalProp3")
    public AdditionalProp3_ getAdditionalProp3() {
        return additionalProp3;
    }

    @JsonProperty("additionalProp3")
    public void setAdditionalProp3(AdditionalProp3_ additionalProp3) {
        this.additionalProp3 = additionalProp3;
    }
}
