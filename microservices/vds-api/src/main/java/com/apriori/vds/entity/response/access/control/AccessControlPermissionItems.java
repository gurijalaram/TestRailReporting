package com.apriori.vds.entity.response.access.control;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "AccessControlPermissionItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class AccessControlPermissionItems extends Pagination {
    private List<AccessControlPermission> items;
}
