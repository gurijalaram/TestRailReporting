package com.apriori.cds.objects.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ApVersionsSchema.json")
@JsonRootName("response")
@Data
public class ApVersions extends Pagination {
    private List<ApVersion> items;
}
