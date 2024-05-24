package com.apriori.shared.util.nexus.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NexusComponent {
    private String agentZipFolder;
    private String agentUnZipFolder;
    private String baseFolder;
    private String installExecutableFile;
    private String webLoginSession;
    private String privateKeyFile;
    private String agentInstallFolder;
    private String agentItemName;
}
