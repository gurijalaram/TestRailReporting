package com.apriori.acs.api.models.response.acs.materialstockmetadata;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/MaterialStockMetadata.json")
public class MaterialStockMetadataResponse extends ArrayList<MaterialStockMetadataItem> {
    private MaterialStockMetadataItem materialStockMetadataItem;
}