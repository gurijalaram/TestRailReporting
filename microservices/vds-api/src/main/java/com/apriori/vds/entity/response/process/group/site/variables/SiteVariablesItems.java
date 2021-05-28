package com.apriori.vds.entity.response.process.group.site.variables;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "SiteVariablesItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteVariablesItems extends Pagination {
    private List<SiteVariable> items;
}