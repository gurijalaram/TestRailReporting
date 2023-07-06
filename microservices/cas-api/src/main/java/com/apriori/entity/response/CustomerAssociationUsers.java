package com.apriori.entity.response;

import com.apriori.utils.common.objects.Paged;
import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "CustomerAssociationUsersSchema.json")
@JsonRootName("response")
@Data
public class CustomerAssociationUsers extends Pagination implements Paged<CustomerAssociationUser> {
    private List<CustomerAssociationUser> items;
}
