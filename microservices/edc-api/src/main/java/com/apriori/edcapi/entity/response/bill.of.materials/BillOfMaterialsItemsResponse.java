package com.apriori.edcapi.entity.response.bill.of.materials;

import com.apriori.annotations.Schema;
import com.apriori.authorization.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "BillOfMaterialsItemsResponse.json")
@Data
@JsonRootName("response")
public class BillOfMaterialsItemsResponse extends Pagination {
    private List<BillOfMaterialsResponse> items;
}
