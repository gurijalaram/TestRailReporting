package com.apriori.cic.api.agent;

import com.apriori.cic.api.enums.AgentOptionFields;
import com.apriori.cic.api.utils.AgentConstants;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.properties.PropertiesContext;

import java.util.ArrayList;
import java.util.List;

public class TeamCenterAgent extends Agent {

    /**
     * set connector options for Team Center agent
     *
     * @param nexusComponent - Component path info
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
     * uninstall team center agent
     *
     * @return Agent Object
     */
    @Override
    public Agent unInstall() {
        ftpClient.UnInstall(getInstallFolder());
        return this;
    }

    /**
     * install Team center agent
     *
     * @param nexusComponent - Component path info
     * @return Agent Object
     */
    public Agent install(NexusComponent nexusComponent) {
        ftpClient.uploadAndInstall(nexusComponent);
        ftpClient.installCertificates(getInstallFolder());
        ftpClient.executeAgentService(getInstallFolder());
        return this;
    }

    /**
     * get Installation folder path for team center agent
     *
     * @return Installation folder
     */
    private String getInstallFolder() {
        return String.format(AgentConstants.REMOTE_TC_INSTALL_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"),
            PropertiesContext.get("env") + "-" + PropertiesContext.get("customer"));
    }

    /**
     * get agent options for team center agent
     *
     * @return List<AgentOptions>
     */
    private List<AgentOptions> getAgentOptions() {
        List<AgentOptions> agentOptionsList = new ArrayList<>();
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AUTH_TOKEN.getAgentOptionField(), AgentOptionFields.AUTH_TOKEN.getAgentOptionField() + PropertiesContext.get("ci-connect.authorization_key")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AGENT_PORT.getAgentOptionField(), AgentOptionFields.AGENT_PORT.getAgentOptionField() + agentPort.getPort().toString()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.INSTALL_DIRECTORY.getAgentOptionField(), AgentOptionFields.INSTALL_DIRECTORY.getAgentOptionField() + "C:" + getInstallFolder()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_USER.getAgentOptionField(), AgentOptionFields.PLM_USER.getAgentOptionField() + PropertiesContext.get("ci-connect.teamcenter.username")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_PASSWORD.getAgentOptionField(), AgentOptionFields.PLM_PASSWORD.getAgentOptionField() + PropertiesContext.get("ci-connect.teamcenter.password")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.HOST_NAME.getAgentOptionField(), AgentOptionFields.HOST_NAME.getAgentOptionField() + PropertiesContext.get("ci-connect.teamcenter.host_name")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.FSC_URL.getAgentOptionField(), AgentOptionFields.FSC_URL.getAgentOptionField() + PropertiesContext.get("ci-connect.teamcenter.fsc_url")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.MAX_PARTS_TO_RETURN.getAgentOptionField(), AgentOptionFields.MAX_PARTS_TO_RETURN.getAgentOptionField() + PropertiesContext.get("ci-connect.maximum_parts")));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.APP_KEY.getAgentOptionField(), AgentOptionFields.APP_KEY.getAgentOptionField() + agentConnectionInfo.getAppKey()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.WSS_URL.getAgentOptionField(), AgentOptionFields.WSS_URL.getAgentOptionField() + agentConnectionOptions.getWssUrl()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.SCAN_RATE.getAgentOptionField(), AgentOptionFields.SCAN_RATE.getAgentOptionField() + agentConnectionOptions.getScanRate()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.AGENT_ID.getAgentOptionField(), AgentOptionFields.AGENT_ID.getAgentOptionField() + agentConnectionOptions.getAgentId()));
        agentOptionsList.add(new AgentOptions(AgentOptionFields.PLM_TYPE.getAgentOptionField(), AgentOptionFields.PLM_TYPE.getAgentOptionField() + agentConnectionOptions.getPlmType()));
        return agentOptionsList;
    }
}