package com.apriori.edc.api.models.response.accounts;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "AccountsItemsResponse.json")
@Data
@JsonRootName("response")
public class AccountsItemsResponse extends Pagination {
    private List<AccountsResponse> items;
}
