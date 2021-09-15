package com.apriori.vds.entity.response.access.control;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

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
