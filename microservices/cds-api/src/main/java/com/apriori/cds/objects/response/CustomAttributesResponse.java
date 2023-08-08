package com.apriori.cds.objects.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "CustomAttributesSchema.json")
@JsonRootName("response")
@Data
public class CustomAttributesResponse extends Pagination {
    private List<CustomAttribute> items;
}