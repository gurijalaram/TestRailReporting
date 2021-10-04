package com.apriori.bcs.entity.response;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;


@Data
@JsonRootName("response")
@Schema(location = "PartsSchema.json")
public class Parts extends Pagination {
    private List<Part> items;
}
