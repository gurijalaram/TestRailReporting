package com.apriori.vds.api.models.response.access.control;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

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
