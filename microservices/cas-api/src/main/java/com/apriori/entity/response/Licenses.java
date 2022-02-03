package com.apriori.entity.response;

import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "LicensesSchema.json")
@Data
@JsonRootName("response")
public class Licenses {
    private List<LicenseResponse> items;
}
