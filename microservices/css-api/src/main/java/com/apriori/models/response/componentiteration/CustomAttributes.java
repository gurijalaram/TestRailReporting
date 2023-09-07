package com.apriori.models.response.componentiteration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomAttributes {
    @JsonProperty("Language")
    private String language;
    private String projectType;
    @JsonProperty("EPAMassembly")
    private String epamAssembly;
    private List<String> tags;
    @JsonProperty("UDA5")
    public Object uda5;
    @JsonProperty("UDA4")
    public Object uda4;
    @JsonProperty("UDA3")
    public String uda3;
    @JsonProperty("UDA1")
    public String uda1;
    @JsonProperty("UDA2")
    public Object uda2;
}
