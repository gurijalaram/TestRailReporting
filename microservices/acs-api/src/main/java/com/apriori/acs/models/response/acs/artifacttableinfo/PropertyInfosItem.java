package com.apriori.acs.models.response.acs.artifacttableinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PropertyInfosItem {
    @JsonProperty("@type")
    private String type;
    private String name;
    private String cachedDisplayName;
    private String propertyAccessor;
    private Boolean isEditable;
    private Boolean isLargeTextField;
    private Boolean hideDelta;
    private Boolean showDeltaIfInvalid;
    private Boolean invertedGoodness;
    private String formatterName;
    private String alignment;
}
