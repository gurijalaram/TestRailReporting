package com.apriori.cic.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlmPartAttribute {
    @JsonProperty("Value")
    private String value;
    @JsonProperty("Display")
    private String display;
    @JsonProperty("Path")
    public String path;
    @JsonProperty("Tooltip")
    public String tooltip;
}
