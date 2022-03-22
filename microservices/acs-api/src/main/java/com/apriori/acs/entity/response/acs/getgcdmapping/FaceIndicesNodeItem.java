package com.apriori.acs.entity.response.acs.getgcdmapping;

import lombok.Data;

import java.util.List;

@Data
public class FaceIndicesNodeItem {
    private ArtifactKeyItem key;
    private List<Integer> value;
}
