package com.apriori.edcapi.entity.response.bill.of.materials;

import com.apriori.apibase.services.Pagination;
import com.apriori.utils.http.enums.Schema;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Schema(location = "BillOfMaterialsItems.json")
@Data
@JsonRootName("response")
public class BillOfMaterialsItems extends Pagination {
    private List<BillOfMaterialsResponse> items;
}
