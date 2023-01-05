package com.apriori.entity.response;

import com.apriori.utils.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(location = "ServiceAccountsSchema.json")
@JsonRootName("response")
@Data
public class ServiceAccounts extends Pagination {
    private List<ServiceAccount> items;
}