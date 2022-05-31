package com.apriori.acs.entity.response.acs.getgcdmapping;

import com.apriori.utils.http.enums.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(location = "acs/GetGcdMappingResponse.json")
public class GetGcdMappingResponse {
    private List<Integer> boundingBox;
    private boolean requiresRegen;
    private List<DrawableNodeItem> drawableNodesByArtifactKeyEntries;
    private List<AxesEntryValueItem> axesEntries;
    private List<FaceIndicesNodeItem> faceIndeciesByArtifactKeyEntries;
}
