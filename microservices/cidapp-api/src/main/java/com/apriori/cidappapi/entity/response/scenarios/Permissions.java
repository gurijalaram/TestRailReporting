package com.apriori.cidappapi.entity.response.scenarios;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Permissions {
    @JsonProperty("READ")
    private String read;
    @JsonProperty("DELETE")
    private String delete;
    @JsonProperty("UPDATE")
    private String update;
}
