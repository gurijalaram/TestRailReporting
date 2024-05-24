package com.apriori.cic.api.agent;

import com.apriori.cic.api.enums.AgentOptionFields;
import com.apriori.cic.api.utils.AgentConstants;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileSystemAgent extends Agent {

    /**
     * set connector options for file system agent
     *
     * @param nexusComponent - component path info
     * @param session        - web session
     * @return Agent object
     */
    @Override
    public Agent setConnectorOptions(NexusComponent nexusComponent, String session) {
        getBasicConnectorOptions(session);
        updateOptionsFile(nexusComponent, getAgentOptions());
        return this;
    }

    /**
     * Uninstall file system agent
     *
     * @return Agent object
     */
    @Override
    public Agent unInstall() {
        ftpClient.UnInstall(getInstallFolder());
        return this;
    }

    /**
     * Install file system agent
     *
     * @param nexusComponent - component path info
     * @return Agent object
     */
    @Override
    public Agent install(NexusComponent nexusComponent) {
        ftpClient.uploadAndInstall(nexusComponent);
        ftpClient.installCertificates(getInstallFolder());
        ftpClient.executeAgentService(getInstallFolder());
        return this;
    }

    /**
     * Get Installation path for file system agent installation
     *
     * @return installation folder
     */
    private String getInstallFolder() {
        return String.format(AgentConstants.REMOTE_FS_INSTALL_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"),
            PropertiesContext.get("env") + "-" + PropertiesContext.get("customer"));
    }

    /**
     * get File system agent root folder for workflows
     *
     * @return remote folder path
     */
    private String getWorkflowRootFolder() {
        return String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"));
    }

    /**
     * get options from file system connector options
     *
     * @return List<AgentOptions>
     */
    private List<AgentOptions> getAgentOptions() {
        List<AgentOptions> agentOptionsList = new ArrayList<>();
        agentOptionsList.add(new AgentOptions(AgentOptionFields.APP_KEY.getAgentOptionField(), AgentOptionFields.APP_KEY.getAgentOptionField() + agentConnectionInfo.getAppKey()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.INSTALL_DIRECTORY.getAgentOptionField(), AgentOptionFields.INSTALL_DIRECTORY.getAgentOptionField() + "C:" + getInstallFolder()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.WSS_URL.getAgentOptionField(), AgentOptionFields.WSS_URL.getAgentOptionField() + agentConnectionOptions.getWssUrl()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.SCAN_RATE.getAgentOptionField(), AgentOptionFields.SCAN_RATE.getAgentOptionField() + agentConnectionOptions.getScanRate()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AGENT_ID.getAgentOptionField(), AgentOptionFields.AGENT_ID.getAgentOptionField() + agentConnectionOptions.getAgentId()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_TYPE.getAgentOptionField(), AgentOptionFields.PLM_TYPE.getAgentOptionField() + agentConnectionOptions.getPlmType()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AGENT_PORT.getAgentOptionField(), AgentOptionFields.AGENT_PORT.getAgentOptionField() + agentPort.getPort().toString()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AUTH_TOKEN.getAgentOptionField(), AgentOptionFields.AUTH_TOKEN.getAgentOptionField() + PropertiesContext.get("ci-connect.authorization_key")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.ROOT_FOLDER_PATH.getAgentOptionField(), AgentOptionFields.ROOT_FOLDER_PATH.getAgentOptionField() + "C:" + getWorkflowRootFolder()));
        return agentOptionsList;
    }
}