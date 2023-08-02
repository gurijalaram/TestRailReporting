package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "SubLicensesSchema.json")
@JsonRootName("response")
@Data
public class SubLicenses extends Pagination {
    private List<SubLicense> items;
}
