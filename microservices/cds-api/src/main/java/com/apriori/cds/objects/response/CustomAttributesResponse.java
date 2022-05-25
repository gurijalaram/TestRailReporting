package com.apriori.cds.objects.response;

import com.apriori.apibase.services.common.objects.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CustomAttributesSchema.json")
@JsonRootName("response")
@Data
public class CustomAttributesResponse extends Pagination {
    private List<CustomAttribute> items;
}