package com.apriori.cic.api.agent;

import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.nexus.models.response.NexusAgentItem;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.nexus.utils.NexusSearchParameters;
import com.apriori.shared.util.nexus.utils.NexusUtil;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

@Slf4j
public class AgentRepository {

    private NexusComponent nexusComponent;
    private NexusAgentItem nexusAgentItem;

    public void AgentRepository() {
        nexusComponent = new NexusComponent();
    }

    /**
     * Search agent repository by group and version in nexus.
     *
     * @return current class object
     */
    public NexusAgentItem searchNexusRepositoryByGroup() {
        NexusSearchParameters nexusSearchParameters = NexusSearchParameters.builder()
            .repositoryName(PropertiesContext.get("ci-connect.nexus_repository"))
            .groupName(PropertiesContext.get("ci-connect.nexus_group"))
            .version(PropertiesContext.get("ci-connect.nexus_version"))
            .fileExtension(PropertiesContext.get("ci-connect.nexus_extension"))
            .build();
        return NexusUtil.searchRepositoryByGroup(nexusSearchParameters);
    }

    /**
     * Download agent executable zip file from nexus repository to local folder
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destination Directory (will be created if does not exists)
     *
     * @return current class object
     */
    public NexusComponent downloadAgent(NexusAgentItem nexusAgentItem) {
        try {
            nexusComponent.setBaseFolder(String.valueOf(FileResourceUtil.createTempDir(null)).toLowerCase());
            nexusComponent.setAgentZipFolder(nexusComponent.getBaseFolder() + File.separator + StringUtils.substringAfterLast(nexusAgentItem.getName(), "/"));
            nexusComponent.setAgentUnZipFolder(nexusComponent.getBaseFolder() + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusAgentItem.getName(), "/")));
            nexusComponent.setAgentItemName(nexusAgentItem.getName());
        } catch (Exception ioException) {
            log.error("PATH NOT FOUND!!");
        }
        nexusComponent = NexusUtil.downloadComponent(nexusAgentItem);
        NexusUtil.extractZip();
        return nexusComponent;
    }
}
