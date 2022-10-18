package com.apriori.acs.entity.response.acs.gcdmapping;

import lombok.Data;

import java.util.List;

@Data
public class AxesEntryValueItem {
    private ArtifactKeyItem key;
    private List<AxesItem> value;
}
