package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "LicensedApplicationSchema.json")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@JsonRootName("response")
public class LicensedApplications extends Pagination {
    private List<LicensedApplication> items;
}