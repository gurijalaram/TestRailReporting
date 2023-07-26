package com.apriori.vds.entity.response.process.group.materials;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ProcessGroupMaterialsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ProcessGroupMaterialsItems extends Pagination {
    private List<ProcessGroupMaterial> items;
}