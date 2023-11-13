package com.apriori.acs.api.models.response.acs.gcdmapping;

import com.apriori.shared.util.annotations.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GcdMappingResponse.json")
public class GcdMappingResponse {
    private List<Integer> boundingBox;
    private boolean requiresRegen;
    private List<DrawableNodeItem> drawableNodesByArtifactKeyEntries;
    private List<AxesEntryValueItem> axesEntries;
    private List<FaceIndicesNodeItem> faceIndeciesByArtifactKeyEntries;
}