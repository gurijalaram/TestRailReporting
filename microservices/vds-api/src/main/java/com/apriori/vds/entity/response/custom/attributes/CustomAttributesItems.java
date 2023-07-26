package com.apriori.vds.entity.response.custom.attributes;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "CustomAttributesItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class CustomAttributesItems extends Pagination {
    private List<CustomAttribute> items;
}
