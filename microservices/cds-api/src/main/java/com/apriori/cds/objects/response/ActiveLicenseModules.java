package com.apriori.cds.objects.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;
import com.apriori.cds.entity.response.LicensedModule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ActiveModulesSchema.json")
@Data
@JsonRootName("response")
public class ActiveLicenseModules extends Pagination {
    private List<LicensedModule> items;
}