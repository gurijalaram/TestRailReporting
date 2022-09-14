package com.apriori.acs.entity.response.acs.getsetproductioninfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentPgIdentifier {
    @JsonProperty("first")
    private String first;
    @JsonProperty("second")
    private String second;
}
