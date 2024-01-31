package com.apriori.shared.util.nexus.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NexusSearchParameters {
    private String repositoryName;
    private String groupName;
    private String version;
    private String fileExtension;
}
