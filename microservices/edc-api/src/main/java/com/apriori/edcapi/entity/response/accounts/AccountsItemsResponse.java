package com.apriori.edcapi.entity.response.accounts;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "AccountsItemsResponse.json")
@Data
@JsonRootName("response")
public class AccountsItemsResponse extends Pagination {
    private List<AccountsResponse> items;
}
