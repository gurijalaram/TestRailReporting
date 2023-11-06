package com.apriori.cds.api.models.response;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CustomAttributesSchema.json")
@JsonRootName("response")
@Data
public class CustomAttributesResponse extends Pagination {
    private List<CustomAttribute> items;
}