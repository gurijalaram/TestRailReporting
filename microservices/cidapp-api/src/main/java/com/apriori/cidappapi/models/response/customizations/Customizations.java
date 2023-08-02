package com.apriori.cidappapi.models.response.customizations;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "Customizations.json")
@JsonRootName("response")
@Data
public class Customizations extends Pagination {
    List<CustomizationItems> items;
}
