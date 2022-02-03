package com.apriori.entity.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "SubLicensesSchema.json")
@Data
@JsonRootName("response")
public class SubLicenses extends Pagination {
    private List<SubLicense> items;
}