package com.apriori.vds.api.models.response.process.group.associations;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

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
