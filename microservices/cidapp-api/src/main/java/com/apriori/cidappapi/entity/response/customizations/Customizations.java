package com.apriori.cidappapi.entity.response.customizations;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

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
