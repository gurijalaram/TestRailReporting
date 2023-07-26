package com.apriori.vds.entity.response.process.group.associations;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ProcessGroupAssociationsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ProcessGroupAssociationsItems extends Pagination {
    private List<ProcessGroupAssociation> items;
}
