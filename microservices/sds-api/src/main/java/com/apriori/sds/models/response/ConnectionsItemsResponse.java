package com.apriori.sds.models.response;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(location = "ConnectionsItems.json")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonRootName("response")
public class ConnectionsItemsResponse extends Pagination {
    private List<Connection> items;
}
