package com.apriori.shared.util.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "LicensedSiteSchemaExpand.json")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteExpand {
    SiteExpandItem response;
    @JsonProperty("_expand")
    private List<String> expand;
}