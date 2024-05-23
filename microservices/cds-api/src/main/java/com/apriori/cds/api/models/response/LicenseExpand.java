package com.apriori.cds.api.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "LicenseExpandSchema.json")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LicenseExpand {
    @JsonProperty("_expand")
    private List<String> expand;
    private LicenseExpandResponse response;
}