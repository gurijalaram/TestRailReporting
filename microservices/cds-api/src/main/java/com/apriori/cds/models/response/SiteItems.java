package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.LicensedApplications;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "SiteItemsSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
public class SiteItems extends Pagination {
    private List<LicensedApplications> items;
}
