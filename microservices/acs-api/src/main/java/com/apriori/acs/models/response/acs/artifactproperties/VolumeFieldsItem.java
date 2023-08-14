package com.apriori.acs.models.response.acs.artifactproperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VolumeFieldsItem {
    @JsonProperty("@type")
    private String type;
    private Double maxToolDiameter;
    private Double totalVolume;
    private Double cornerPickCount;
    private Double cornerPickVolume;
}
