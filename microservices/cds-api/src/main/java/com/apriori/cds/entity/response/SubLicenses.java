package com.apriori.cds.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "SubLicensesSchema.json")
@JsonRootName("response")
@Data
public class SubLicenses extends Pagination {
    private List<SubLicense> items;
}
