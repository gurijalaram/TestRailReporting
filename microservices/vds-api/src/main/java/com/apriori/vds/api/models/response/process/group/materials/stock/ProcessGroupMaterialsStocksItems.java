package com.apriori.vds.api.models.response.process.group.materials.stock;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ProcessGroupMaterialsStocksItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ProcessGroupMaterialsStocksItems extends Pagination {
    private List<ProcessGroupMaterialStock> items;
}