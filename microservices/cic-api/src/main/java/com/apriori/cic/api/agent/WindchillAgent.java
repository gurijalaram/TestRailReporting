package com.apriori.cic.api.agent;

import com.apriori.cic.api.enums.AgentOptionFields;
import com.apriori.cic.api.utils.AgentConstants;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.properties.PropertiesContext;

import java.util.ArrayList;
import java.util.List;

public class WindchillAgent extends Agent {

    /**
     * set connector options for windchill agent
     *
     * @param nexusComponent - component path info
     * @param session        - web session
     * @return Agent Object
     */
    @Override
    public Agent setConnectorOptions(NexusComponent nexusComponent, String session) {
        getBasicConnectorOptions(session);
        updateOptionsFile(nexusComponent, getAgentOptions());
        return this;
    }

    /**
     * uninstall windchill agent
     *
     * @return Agent Object
     */
    @Override
    public Agent unInstall() {
        ftpClient.UnInstall(getInstallFolder());
        return this;
    }

    /**
     * Install windchill agent
     *
     * @param nexusComponent - component path info
     * @return Agent Object
     */
    public Agent install(NexusComponent nexusComponent) {
        ftpClient.uploadAndInstall(nexusComponent);
        ftpClient.installCertificates(getInstallFolder());
        ftpClient.executeAgentService(getInstallFolder());
        return this;
    }

    /**
     * get installation folder path info for windchill agent
     *
     * @return installation folder
     */
    private String getInstallFolder() {
        return String.format(AgentConstants.REMOTE_WC_INSTALL_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"),
            PropertiesContext.get("env") + "-" + PropertiesContext.get("customer"));
    }

    /**
     * get connector options for windchill agent
     *
     * @return List<AgentOptions>
     */
    private List<AgentOptions> getAgentOptions() {
        List<AgentOptions> agentOptionsList = new ArrayList<>();
        agentOptionsList.add(new AgentOptions(AgentOptionFields.RECONNECTION_INTERVAL.getAgentOptionField(), AgentOptionFields.RECONNECTION_INTERVAL.getAgentOptionField() + "3"));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AUTH_TOKEN.getAgentOptionField(), AgentOptionFields.AUTH_TOKEN.getAgentOptionField() + PropertiesContext.get("ci-connect.authorization_key")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AGENT_PORT.getAgentOptionField(), AgentOptionFields.AGENT_PORT.getAgentOptionField() + agentPort.getPort().toString()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.INSTALL_DIRECTORY.getAgentOptionField(), AgentOptionFields.INSTALL_DIRECTORY.getAgentOptionField() + "C:" + getInstallFolder()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.ROOT_FOLDER_PATH.getAgentOptionField(), AgentOptionFields.ROOT_FOLDER_PATH.getAgentOptionField() + "C:" + AgentConstants.REMOTE_ROOT_FILE_PATH_FOLDER));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_USER.getAgentOptionField(), AgentOptionFields.PLM_USER.getAgentOptionField() + agentCredentials.getPlmUser()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_PASSWORD.getAgentOptionField(), AgentOptionFields.PLM_PASSWORD.getAgentOptionField() + agentCredentials.getPlmPassword()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.HOST_NAME.getAgentOptionField(), AgentOptionFields.HOST_NAME.getAgentOptionField() + PropertiesContext.get("ci-connect.windchill.host_name")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.MAX_PARTS_TO_RETURN.getAgentOptionField(), AgentOptionFields.MAX_PARTS_TO_RETURN.getAgentOptionField() + PropertiesContext.get("ci-connect.maximum_parts")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.APP_KEY.getAgentOptionField(), AgentOptionFields.APP_KEY.getAgentOptionField() + agentConnectionInfo.getAppKey()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.WSS_URL.getAgentOptionField(), AgentOptionFields.WSS_URL.getAgentOptionField() + agentConnectionOptions.getWssUrl()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.SCAN_RATE.getAgentOptionField(), AgentOptionFields.SCAN_RATE.getAgentOptionField() + agentConnectionOptions.getScanRate()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AGENT_ID.getAgentOptionField(), AgentOptionFields.AGENT_ID.getAgentOptionField() + agentConnectionOptions.getAgentId()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_TYPE.getAgentOptionField(), AgentOptionFields.PLM_TYPE.getAgentOptionField() + agentConnectionOptions.getPlmType()));
        return agentOptionsList;
    }
}