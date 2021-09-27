package com.apriori.vds.entity.response.process.group.site.variable;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ProcessGroupSiteVariablesItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class SiteVariablesItems extends Pagination {
    private List<SiteVariable> items;
}