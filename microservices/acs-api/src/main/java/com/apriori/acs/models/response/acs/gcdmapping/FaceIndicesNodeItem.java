package com.apriori.acs.models.response.acs.gcdmapping;

import lombok.Data;

import java.util.List;

@Data
public class FaceIndicesNodeItem {
    private ArtifactKeyItem key;
    private List<Integer> value;
}
