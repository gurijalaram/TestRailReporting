package com.apriori.edc.api.models.response.bill.of.materials;

import com.apriori.shared.util.annotations.Schema;
import com.apriori.shared.util.models.response.Pagination;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "BillOfMaterialsItemsResponse.json")
@Data
@JsonRootName("response")
public class BillOfMaterialsItemsResponse extends Pagination {
    private List<BillOfMaterialsResponse> items;
}
