package com.apriori.vds.models.response.customizations;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "CustomizationsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class CustomizationsItems extends Pagination {
    private List<Customization> items;
}
