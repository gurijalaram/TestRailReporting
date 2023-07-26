package com.apriori.vds.entity.response.access.control;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "AccessControlGroupItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class AccessControlGroupItems extends Pagination {
    private List<AccessControlGroup> items;
}
