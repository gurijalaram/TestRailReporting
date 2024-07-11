package com.apriori.acs.api.models.response.acs.materialmetadata;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.ArrayList;

@Data
@Schema(location = "acs/MaterialMetadata.json")
public class MaterialMetadataResponse extends ArrayList<MaterialMetadataItem> {
    private MaterialMetadataItem materialMetadataItem;
}
