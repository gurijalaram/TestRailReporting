package com.apriori.shared.util.models.response;

import com.apriori.shared.util.annotations.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private List<String> _expand;
    SiteExpandItem response;

}