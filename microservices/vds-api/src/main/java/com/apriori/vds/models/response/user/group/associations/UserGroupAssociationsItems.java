package com.apriori.vds.models.response.user.group.associations;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "UserGroupAssociationsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class UserGroupAssociationsItems extends Pagination {
    private List<UserGroupAssociation> items;
}
