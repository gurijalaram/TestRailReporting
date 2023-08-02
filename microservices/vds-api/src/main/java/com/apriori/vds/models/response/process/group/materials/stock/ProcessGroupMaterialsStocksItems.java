package com.apriori.vds.models.response.process.group.materials.stock;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

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