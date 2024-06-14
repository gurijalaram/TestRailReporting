package com.apriori.cic.api.agent;

import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.response.AgentConnectionInfo;
import com.apriori.cic.api.models.response.AgentConnectionOptions;
import com.apriori.cic.api.models.response.ConnectorInfo;
import com.apriori.cic.api.utils.AgentCredentials;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.nexus.utils.NexusComponent;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Agent {

    protected AgentCredentials agentCredentials;
    protected FtpClient ftpClient;
    protected AgentConnectionOptions agentConnectionOptions;
    protected AgentPort agentPort;
    protected ConnectorInfo connectorInfo;
    protected AgentConnectionInfo agentConnectionInfo;

    public Agent() {
        agentCredentials = new AgentCredentials().getAgentCredentials();
        ftpClient = new FtpClient();
        agentPort = CicApiTestUtil.getAgentPortData();
    }

    public abstract Agent unInstall();

    public abstract Agent install(NexusComponent nexusComponent);

    public abstract Agent setConnectorOptions(NexusComponent nexusComponent, String session);

    /**
     * Get basic connector options from connector
     *
     * @return AgentConnectionOptions class object
     */
    protected void getBasicConnectorOptions(String session) {
        connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), session);
        agentConnectionInfo = CicApiTestUtil.getAgentConnectorOptions(connectorInfo.getName(), session);
        agentConnectionOptions = AgentConnectionOptions.builder()
            .agentName(connectorInfo.getName())
            .appKey(agentConnectionInfo.getAppKey())
            .wssUrl(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "url=", "\n\n#"))
            .scanRate(Integer.valueOf(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "scanRate=", "\n\n#")))
            .plmType(StringUtils.substringAfter(agentConnectionInfo.getConnectionInfo(), "plmType=").replace(")", ""))
            .agentId(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "agentId=", "\n\n#"))
            .build();
    }

    /**
     * Get connector status information to verify its connected to PLM
     *
     * @return ConnectorInfo class object
     */
    public ConnectorInfo getConnectorStatusInfo(String session) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(5);
        ConnectorInfo connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), session);
        try {
            while (!(connectorInfo.getConnectionStatus().equals("Connected to PLM"))) {
                if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                    break;
                }
                TimeUnit.SECONDS.sleep(30);
                connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), session);
            }
            String isConnected = (connectorInfo.getConnectionStatus().equals("Connected to PLM")) ? "CONNECTED" : "NOT CONNECTED";
            log.info(String.format("CONNECTOR (%s) TO PLM ---%s", isConnected, connectorInfo.getDisplayName()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return connectorInfo;
    }

    /**
     * Update downloaded agent options file with connector information
     *
     * @return current class object
     */
    protected void updateOptionsFile(NexusComponent nexusComponent, List<AgentOptions> agentOptions) {
        File optionsFile = new File(FileResourceUtil.findFileWithExtension(Paths.get(nexusComponent.getAgentUnZipFolder()), "ini"));
        List<String> optionFileContent;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            log.info(String.format("OPTIONS.INI FOUND IN (%s) FILE PATH -- ", optionsFile.getCanonicalPath()));
            optionFileContent = FileUtils.readLines(optionsFile, StandardCharsets.UTF_8);
            for (String line : optionFileContent) {
                for (AgentOptions agentOption : agentOptions) {
                    if (line.equals(agentOption.getAgentFieldName())) {
                        line = line.replace(agentOption.getAgentFieldName(), agentOption.getAgentFieldValue());
                        break;
                    }
                }
                stringBuilder.append(line).append("\n");
            }
            if (optionsFile.exists()) {
                optionsFile.delete();
                log.info("EXISTING OPTIONS.INI FILE IS DELETED");
            }
            optionsFile.setWritable(true);
            FileUtils.writeStringToFile(optionsFile, stringBuilder.toString(), StandardCharsets.UTF_8);
            log.info(String.format("UPDATED THE OPTIONS.INI FILE SUCCESSFULLY!!!  %s", optionsFile.getName()));
        } catch (IOException ioException) {
            throw new RuntimeException("OPTIONS.INI FILE NOT FOUND!! " + ioException);
        }
    }

    /**
     * close ftp connection
     */
    public void closeConnection() {
        ftpClient.close();
    }

    /**
     * get Agent Port
     *
     * @return AgentPort
     */
    public AgentPort getAgentPort() {
        return agentPort;
    }
}