package com.apriori.cds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "UserPropertiesSchema.json")
@Data
@JsonRootName("response")
public class UserProperties extends Pagination {
    private List<RequiredProperties> items;
}