package com.apriori.acs.api.models.response.acs.gcdmapping;

import lombok.Data;

import java.util.List;

@Data
public class FaceIndicesNodeItem {
    private ArtifactKeyItem key;
    private List<Integer> value;
}
