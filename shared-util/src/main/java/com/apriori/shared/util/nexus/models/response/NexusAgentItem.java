package com.apriori.shared.util.nexus.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NexusAgentItem {
    private String id;
    private String repository;
    private String format;
    private String group;
    private String name;
    private Object version;
    private List<NexusAsset> assets;
    private List<Object> tags;
}
