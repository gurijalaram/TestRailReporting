package com.apriori.cic.api.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlmPartTypeIconField {
    @JsonProperty("Path")
    public String path;
    @JsonProperty("Tooltip")
    public String tooltip;
}
