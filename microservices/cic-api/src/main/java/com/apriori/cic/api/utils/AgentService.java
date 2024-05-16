package com.apriori.cic.api.utils;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.ConnectorRequest;
import com.apriori.cic.api.models.response.AgentConnectionInfo;
import com.apriori.cic.api.models.response.AgentConnectionOptions;
import com.apriori.cic.api.models.response.ConnectorInfo;
import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.nexus.models.response.NexusAgentItem;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.nexus.utils.NexusSearchParameters;
import com.apriori.shared.util.nexus.utils.NexusUtil;
import com.apriori.shared.util.properties.PropertiesContext;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@Data
@AllArgsConstructor
public class AgentService {
    private static String installExecutableFile;
    private static String webLoginSession;

    private Session jschSession = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;
    private AgentCredentials agentCredentials;
    private NexusAgentItem nexusAgentItem = null;
    private AgentConnectionOptions agentConnectionOptions = null;
    private ConnectorInfo connectorInfo = null;
    private NexusComponent agentData;
    private NexusSearchParameters nexusSearchParameters;
    private AgentPort agentPort;
    private RequestEntityUtil requestEntityUtil;

    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    public AgentService() {
        agentCredentials = new AgentCredentials().getAgentCredentials();
        agentData = new NexusComponent();
        agentPort = CicApiTestUtil.getAgentPortData();
        requestEntityUtil = RequestEntityUtilBuilder.useRandomUser(APRIORI_DEVELOPER);
    }

    /**
     * Create Session to connect to VM
     *
     * @return current class object
     */
    @SneakyThrows
    public AgentService createRemoteSession() {
        JSch jsch = new JSch();
        try {
            agentData.setPrivateKeyFile(agentData.getBaseFolder() + File.separator + "key" + File.separator +
                StringUtils.substringAfterLast(AgentConstants.AWS_SYSTEM_PARAMETER_PRIVATE_KEY, "/"));
            String privateKey = AwsParameterStoreUtil.getSystemParameter(AgentConstants.AWS_SYSTEM_PARAMETER_PRIVATE_KEY);
            log.info("########## PRIVATE KEY RETRIEVED FROM AWS SUCCESSFULLY. ########  " + agentData.getPrivateKeyFile());
            FileUtils.writeStringToFile(new File(agentData.getPrivateKeyFile()), privateKey, StandardCharsets.UTF_8);
            jsch.addIdentity(agentData.getPrivateKeyFile(), agentCredentials.getPassword());
            log.debug("########## PRIVATE KEY ADDED SUCCESSFULLY. ########");
            jschSession = jsch.getSession(agentCredentials.getUsername(), agentCredentials.getHost(), Integer.parseInt(agentCredentials.getPort()));
            log.debug("########## SESSION CREATED SUCCESSFULLY. ########");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);
            jschSession.connect(SESSION_TIMEOUT);
        } catch (Exception connectionException) {
            log.error("SESSION CONNECTION FAILED!!" + connectionException.getMessage());
            throw new Exception("Session connection failed");
        }
        return this;
    }

    /**
     * Create and SFTP connection to VM to delete the folder and upload the files.
     *
     * @return current class object
     */
    public AgentService getSftpConnection() {
        try {
            channel = jschSession.openChannel("sftp");
            channel.connect(CHANNEL_TIMEOUT);
            log.info("########## CONNECTED TO VM SUCCESSFULLY. ########");
            channelSftp = (ChannelSftp) channel;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return this;
    }

    /**
     * close sftp and session connections
     */
    public void close() {
        channelSftp.disconnect();
        channel.disconnect();
        jschSession.disconnect();
    }

    /**
     * Upload download nexus repository from local copy to VM and installs the agent
     *
     * @return current class Object
     */
    public AgentService uploadAndInstall() {
        String remoteDownloadFolder = AgentConstants.REMOTE_DOWNLOAD_FOLDER + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusAgentItem.getName(), "/"));
        try {
            recursiveFolderDelete(AgentConstants.REMOTE_DOWNLOAD_FOLDER + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusAgentItem.getName(), "/")));
            recursiveFolderUpload(agentData.getAgentUnZipFolder(), AgentConstants.REMOTE_DOWNLOAD_FOLDER);
            log.info("########## COPIED AGENT INSTALLER FOLDER TO VM ########");
            channelSftp.cd(remoteDownloadFolder);
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls("*");
            String executableFile = list.stream()
                .filter(ls -> ls.getFilename().endsWith("exe"))
                .findFirst()
                .get()
                .getFilename();

            execute(remoteDownloadFolder + File.separator + executableFile + " -q");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return this;
    }

    /**
     * Uninstall the agent from VM and delete all the folders
     *
     * @return current class object
     */
    public AgentService cleanUnInstall() {
        String executableFile = StringUtils.EMPTY;
        try {
            channelSftp.cd(this.getInstallFolder());
            Vector<ChannelSftp.LsEntry> list = channelSftp.ls("*");
            try {
                executableFile = list.stream()
                    .filter(f -> f.getFilename().contains("uninstall"))       // check end with
                    .findFirst()
                    .get()
                    .getFilename();
            } catch (Exception noSuchElementException) {
                log.warn("UNINSTALL EXECUTABLE FILE NOT FOUND!!!");
            }
            if (!executableFile.isEmpty()) {
                execute(this.getInstallFolder() + File.separator + executableFile + " -q");
            }
            if (recursiveFolderDelete(this.getInstallFolder())) {
                log.info("AGENT UNINSTALLED AND DELETED THE FOLDER SUCCESSFULLY!!!!");
            }
        } catch (SftpException sftpException) {
            log.error(sftpException.getMessage());
        }
        return this;
    }

    /**
     * Search agent repository by group and version in nexus.
     *
     * @return current class object
     */
    public AgentService searchNexusRepositoryByGroup() {
        nexusSearchParameters = NexusSearchParameters.builder()
            .repositoryName(PropertiesContext.get("ci-connect.nexus_repository"))
            .groupName(PropertiesContext.get("ci-connect.nexus_group"))
            .version(PropertiesContext.get("ci-connect.nexus_version"))
            .fileExtension(PropertiesContext.get("ci-connect.nexus_extension"))
            .build();
        nexusAgentItem = NexusUtil.searchRepositoryByGroup(nexusSearchParameters);
        return this;
    }

    /**
     * Download agent executable zip file from nexus repository to local folder
     *
     * @return current class object
     */
    public AgentService downloadAgentFile() {
        agentData = NexusUtil.downloadComponent();
        return this;
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     */
    public AgentService unZip() {
        NexusUtil.extractZip();
        return this;
    }

    /**
     * Update downloaded agent options file with connector information
     *
     * @return current class object
     */
    public AgentService updateOptionsFile() {
        File optionsFile = new File(findFileWithExtension(Paths.get(agentData.getAgentUnZipFolder()), "ini"));
        List<String> optionFileContent;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            log.info(String.format("OPTIONS.INI FOUND IN (%s) FILE PATH -- ", optionsFile.getCanonicalPath()));
            if (optionsFile.exists()) {
                optionFileContent = FileUtils.readLines(optionsFile, StandardCharsets.UTF_8);
                for (String line : optionFileContent) {
                    switch (line) {
                        case "installDirectory=":
                            stringBuilder.append(line).append(agentConnectionOptions.getInstallDirectory()).append("\n");
                            break;
                        case "url=":
                            stringBuilder.append(line).append(agentConnectionOptions.getWssUrl()).append("\n");
                            break;
                        case "appKey=":
                            stringBuilder.append(line).append(agentConnectionOptions.getAppKey()).append("\n");
                            break;
                        case "scanRate=":
                            stringBuilder.append(line).append(agentConnectionOptions.getScanRate().toString()).append("\n");
                            break;
                        case "agentId=":
                            stringBuilder.append(line).append(agentConnectionOptions.getAgentId()).append("\n");
                            break;
                        case "port=":
                            stringBuilder.append(line).append(agentConnectionOptions.getPort().toString()).append("\n");
                            break;
                        case "plmType=":
                            stringBuilder.append(line).append(agentConnectionOptions.getPlmType()).append("\n");
                            break;
                        case "auth-token=":
                            stringBuilder = (null == agentConnectionOptions.getAuthToken()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getAuthToken()).append("\n");
                            break;
                        case "reconnectionInterval=":
                            stringBuilder = (null == agentConnectionOptions.getReconnectionInterval()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getReconnectionInterval().toString()).append("\n");
                            break;
                        case "hostName=":
                            stringBuilder = (null == agentConnectionOptions.getHostName()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getHostName()).append("\n");
                            break;
                        case "user=":
                            stringBuilder = (null == agentConnectionOptions.getPlmUser()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getPlmUser()).append("\n");
                            break;
                        case "password=":
                            stringBuilder = (null == agentConnectionOptions.getPlmPassword()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getPlmPassword()).append("\n");
                            break;
                        case "fscUrl=":
                            stringBuilder = (null == agentConnectionOptions.getFscUrl()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getFscUrl()).append("\n");
                            break;
                        case "rootFolderPath=":
                            stringBuilder = (null == agentConnectionOptions.getRootFolderPath()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getRootFolderPath()).append("\n");
                            break;
                        case "maxPartsToReturn=":
                            stringBuilder = (null == agentConnectionOptions.getMaxPartsToReturn()) ? stringBuilder.append(line).append("\n") :
                                stringBuilder.append(line).append(agentConnectionOptions.getMaxPartsToReturn()).append("\n");
                            break;
                        default:
                            stringBuilder.append(line).append("\n");
                    }
                }
                if (optionsFile.exists()) {
                    optionsFile.delete();
                    log.info("EXISTING OPTIONS.INI FILE IS DELETED");
                }
                optionsFile.setWritable(true);
                FileUtils.writeStringToFile(optionsFile, stringBuilder.toString(), StandardCharsets.UTF_8);
                log.info(String.format("UPDATED THE OPTIONS.INI FILE SUCCESSFULLY!!!  %s", optionsFile.getName()));
            }
        } catch (IOException ioException) {
            log.error("OPTIONS.INI FILE NOT FOUND!! " + ioException);
        }
        return this;
    }

    /**
     * Get connector if exists or creates a connector if does not exist in ci-connect.
     *
     * @param loginSession JSESSION ID
     * @return current class object
     */
    public AgentService getConnector(String loginSession) {
        webLoginSession = loginSession;
        ConnectorRequest connectorRequestDataBuilder = null;
        connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), loginSession);
        try {
            connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), loginSession);
        } catch (Exception e) {
            log.info("CONNECTOR NOT FOUND WITH NAME - " + agentPort.getConnector());
            throw new IllegalArgumentException(e);
        }
        if (null == connectorInfo) {
            switch (PropertiesContext.get("ci-connect.agent_type")) {
                case "windchill":
                    connectorRequestDataBuilder = CicApiTestUtil.getConnectorBaseData();
                    connectorRequestDataBuilder.setDisplayName(agentPort.getConnector());
                    ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateConnector(connectorRequestDataBuilder, loginSession);
                    if (responseWrapper.getBody().contains("true")) {
                        log.info("CREATED CONNECTOR WITH NAME - " + agentPort.getConnector());
                    }
            }
            connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), loginSession);
        } else {
            log.info("FOUND CONNECTOR WITH NAME - " + agentPort.getConnector());
        }
        return this;
    }

    /**
     * Get connector options information.
     *
     * @return current class object
     */
    public AgentService getConnectorOptions() {
        agentConnectionOptions = getBasicConnectorOptions();
        switch (PropertiesContext.get("ci-connect.agent_type")) {
            case "windchill":
                agentConnectionOptions.setReconnectionInterval(3);
                agentConnectionOptions.setAuthToken(PropertiesContext.get("ci-connect.authorization_key"));
                agentConnectionOptions.setPort(agentPort.getPort());
                agentConnectionOptions.setInstallDirectory("C:" + this.getInstallFolder(AgentConstants.REMOTE_WC_INSTALL_FOLDER));
                agentConnectionOptions.setRootFolderPath("C:" + AgentConstants.REMOTE_ROOT_FILE_PATH_FOLDER);
                agentConnectionOptions.setPlmUser(agentCredentials.getPlmUser());
                agentConnectionOptions.setPlmPassword(agentCredentials.getPlmPassword());
                agentConnectionOptions.setHostName(PropertiesContext.get("ci-connect.windchill.host_name"));
                agentConnectionOptions.setMaxPartsToReturn(PropertiesContext.get("ci-connect.maximum_parts"));
                break;
            case "teamcenter":
                agentConnectionOptions.setInstallDirectory("C:" + this.getInstallFolder(AgentConstants.REMOTE_TC_INSTALL_FOLDER));
                agentConnectionOptions.setPort(agentPort.getPort());
                agentConnectionOptions.setAuthToken(PropertiesContext.get("ci-connect.authorization_key"));
                agentConnectionOptions.setHostName(PropertiesContext.get("ci-connect.teamcenter.host_name"));
                agentConnectionOptions.setPlmUser(PropertiesContext.get("ci-connect.teamcenter.username"));
                agentConnectionOptions.setPlmPassword(PropertiesContext.get("ci-connect.teamcenter.password"));
                agentConnectionOptions.setFscUrl(PropertiesContext.get("ci-connect.teamcenter.fsc_url"));
                agentConnectionOptions.setMaxPartsToReturn(PropertiesContext.get("ci-connect.maximum_parts"));
                break;
            case "filesystem":
                agentConnectionOptions.setInstallDirectory("C:" + this.getInstallFolder(AgentConstants.REMOTE_FS_INSTALL_FOLDER));
                agentConnectionOptions.setPort(agentPort.getPort());
                agentConnectionOptions.setAuthToken(PropertiesContext.get("ci-connect.authorization_key"));
                agentConnectionOptions.setRootFolderPath(("C:" + String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"))));
                break;
        }
        return this;
    }

    /**
     * Install certificates after installation of agent
     * Amazing i
     */
    public AgentService installCertificates() {
        String jreBinDirectory = StringUtils.EMPTY;
        jreBinDirectory = this.getInstallFolder() + File.separator + "jre/bin/";
        try {
            channelSftp.cd(jreBinDirectory);
            execute(jreBinDirectory + "keytool -importcert -file " + AgentConstants.REMOTE_CERTIFICATE_FOLDER + "/fbc-consvdc02-CA-2026.cer -alias fbc-consvdc02-2026 -keystore \"" + this.getInstallFolder() + "/jre/lib/security/cacerts\" -storepass changeit -noprompt");
            execute(jreBinDirectory + "keytool -importcert -file " + AgentConstants.REMOTE_CERTIFICATE_FOLDER + "/fbc1-2040.cer -alias fbc1-2040 -keystore \"" + this.getInstallFolder() + "/jre/lib/security/cacerts\" -storepass changeit -noprompt");
        } catch (Exception e) {
            log.error("FAILED TO IMPORT CERTIFICATES");
            throw new IllegalArgumentException(e);
        }
        return this;
    }

    /**
     * Restart Agent Service
     *
     * @return current class object
     */
    public AgentService executeAgentService() {
        String agentService = this.getInstallFolder();
        runService(agentService, "stop");
        runService(agentService, "start");
        return this;
    }


    /**
     * Get connector status information to verify its connected to PLM
     *
     * @return ConnectorInfo class object
     */
    public ConnectorInfo getConnectorStatusInfo() {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(5);
        ConnectorInfo connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), webLoginSession);
        try {
            while (!(connectorInfo.getConnectionStatus().equals("Connected to PLM"))) {
                if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                    break;
                }
                TimeUnit.SECONDS.sleep(30);
                connectorInfo = CicApiTestUtil.getMatchedConnector(agentPort.getConnector(), webLoginSession);
            }
            String isConnected = (connectorInfo.getConnectionStatus().equals("Connected to PLM")) ? "CONNECTED" : "NOT CONNECTED";
            log.info(String.format("CONNECTOR (%s) TO PLM ---%s", isConnected, connectorInfo.getDisplayName()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return connectorInfo;
    }

    /**
     * start agent service
     *
     * @param agentService   service Name
     * @param serviceRunType (start, stop or restart)
     */
    private void runService(String agentService, String serviceRunType) {
        try {
            channelSftp.cd(agentService);
            switch (serviceRunType) {
                case "start":
                    execute(agentService + File.separator + String.format("nssm start \"aPriori Agent - %s\"", StringUtils.substringAfterLast(agentService, "/")));
                    break;
                case "stop":
                    execute(agentService + File.separator + String.format("nssm stop \"aPriori Agent - %s\"", StringUtils.substringAfterLast(agentService, "/")));
                    break;
                case "restart":
                    execute(agentService + File.separator + String.format("nssm restart \"aPriori Agent - %s\"", StringUtils.substringAfterLast(agentService, "/")));
                    break;
            }
        } catch (Exception e) {
            log.error("FAILED TO RUN SERVICE!!");
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Execute coommand on remote VM machine using JSch open channel sftp connection
     *
     * @param command command to execute
     */
    private String execute(String command) {
        int exitStatus = -1;
        StringBuilder stringBuilder = new StringBuilder();
        String status = StringUtils.EMPTY;
        byte[] buffer = new byte[1024];
        try {
            channel = (Channel) jschSession.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream input = channel.getInputStream();
            channel.connect();
            log.info("Channel Connected to machine " + agentCredentials.getHost() + " server with command: " + command);
            while (true) {
                stringBuilder.append(IOUtils.toString(input, StandardCharsets.UTF_8));
                if (channel.isClosed()) {
                    if (input.available() > 0) {
                        continue;
                    }
                    exitStatus = channel.getExitStatus();
                    break;
                }
            }
            status = (stringBuilder.toString().contains("\u0000")) ? stringBuilder.toString().replaceAll("\u0000", "") : stringBuilder.toString();
            log.info("COMMAND EXECUTION STATUS : " + status);
            log.info(String.format("Exit status of the execution: %s ", (exitStatus == 0) ? exitStatus + "(FINISHED EXECUTION)" : exitStatus + "(EXECUTION FAILED)"));
        } catch (JSchException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return status;
    }

    /**
     * Find the downloaded file with zip extension
     *
     * @param path          path to search for file
     * @param fileExtension extension of file to search
     * @return file with complete path
     */
    private String findFileWithExtension(Path path, String fileExtension) {
        String fileWithExtension = "";
        try {
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException("Path must be a directory!");
            }
        } catch (Exception ioException) {
            log.error("PATH NOT FOUND!!");
        }

        try (Stream<Path> walk = Files.walk(Paths.get(String.valueOf(path)))) {
            fileWithExtension = walk
                .filter(p -> !Files.isDirectory(p))   // not a directory
                .map(p -> p.toString().toLowerCase()) // convert path to string
                .filter(f -> f.endsWith(fileExtension))       // check end with
                .findFirst()
                .get();
        } catch (Exception ioException) {
            log.error("FILE NOT FOUND!!");
        }
        return fileWithExtension;
    }

    /**
     * Delete file and folder on remote VM
     *
     * @param path folder to be deleted
     * @return boolean (true or false) indicates succesfull deletion.
     */
    private Boolean recursiveFolderDelete(String path) {
        Boolean isDeleted = false;
        try {
            channelSftp.cd(path); // Change Directory on SFTP Server
            // List source directory structure.
            Vector<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);
            // Iterate objects in the list to get file/folder names.
            for (ChannelSftp.LsEntry item : fileAndFolderList) {
                // If it is a file (not a directory).
                if (!item.getAttrs().isDir()) {
                    channelSftp.rm(path + "/" + item.getFilename()); // Remove file.
                } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
                    try {
                        // removing sub directory.
                        channelSftp.rmdir(path + "/" + item.getFilename());
                    } catch (Exception e) { // If subdir is not empty and error occurs.
                        // Do lsFolderRemove on this subdir to enter it and clear its contents.
                        recursiveFolderDelete(path + "/" + item.getFilename());
                    }
                }
            }
            channelSftp.rmdir(path); // delete the parent directory after empty
            isDeleted = true;
        } catch (Exception e) {
            log.warn(String.format(
                "Either directory >> %s << not exists or already deleted from remote agent VM",
                path
            ));
        }
        return isDeleted;
    }

    /**
     * This method is called recursively to Upload the local folder content to SFTP
     * server
     *
     * @param sourcePath
     * @param destinationPath
     */
    private void recursiveFolderUpload(String sourcePath, String destinationPath) {
        try {
            File sourceFile = new File(sourcePath);
            if (sourceFile.isFile()) {
                // copy if it is a file
                channelSftp.cd(destinationPath);
                if (!sourceFile.getName().startsWith(".")) {
                    channelSftp.put(new FileInputStream(sourceFile), sourceFile.getName(), ChannelSftp.OVERWRITE);
                }
            } else {
                log.info("inside directory " + sourceFile.getName());
                File[] files = sourceFile.listFiles();
                if (files != null && !sourceFile.getName().startsWith(".")) {
                    channelSftp.cd(destinationPath);
                    SftpATTRS attrs = null;
                    // check if the directory is already existing
                    try {
                        attrs = channelSftp.stat(destinationPath + "/" + sourceFile.getName());
                    } catch (Exception e) {
                        log.error(destinationPath + "/" + sourceFile.getName() + " not found");
                    }
                    // else create a directory
                    if (attrs != null) {
                        log.info("Directory exists IsDir=" + attrs.isDir());
                    } else {
                        log.info("Creating dir " + sourceFile.getName());
                        channelSftp.mkdir(sourceFile.getName());
                    }
                    for (File f : files) {
                        recursiveFolderUpload(f.getAbsolutePath(), destinationPath + "/" + sourceFile.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                "DESTINATION FOLDER (%s) ON REMOTE SERVER NOT FOUND!!",
                destinationPath));
        }
    }

    /**
     * Get installation folder for each customer and agent tpe
     *
     * @return installation path
     */
    private String getInstallFolder() {
        String installFolder = StringUtils.EMPTY;
        switch (PropertiesContext.get("ci-connect.agent_type")) {
            case "windchill":
                installFolder = getInstallFolder(AgentConstants.REMOTE_WC_INSTALL_FOLDER);
                break;
            case "teamcenter":
                installFolder = getInstallFolder(AgentConstants.REMOTE_TC_INSTALL_FOLDER);
                break;
            case "filesystem":
                installFolder = getInstallFolder(AgentConstants.REMOTE_FS_INSTALL_FOLDER);
                break;
        }
        return installFolder;
    }

    /**
     * Get installation folder for each agent type
     *
     * @param agentInstallFolder - Agent type folder
     * @return path of folder from server
     */
    private String getInstallFolder(String agentInstallFolder) {
        return String.format(agentInstallFolder, PropertiesContext.get("env"), PropertiesContext.get("customer"),
            PropertiesContext.get("env") + "-" + PropertiesContext.get("customer"));
    }

    /**
     * Get basic connector options from connector
     *
     * @return AgentConnectionOptions class object
     */
    private AgentConnectionOptions getBasicConnectorOptions() {
        AgentConnectionInfo agentConnectionInfo = CicApiTestUtil.getAgentConnectorOptions(connectorInfo.getName(), webLoginSession);
        return AgentConnectionOptions.builder()
            .agentName(connectorInfo.getName())
            .appKey(agentConnectionInfo.getAppKey())
            .wssUrl(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "url=", "\n\n#"))
            .scanRate(Integer.valueOf(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "scanRate=", "\n\n#")))
            .plmType(StringUtils.substringAfter(agentConnectionInfo.getConnectionInfo(), "plmType=").replace(")", ""))
            .agentId(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "agentId=", "\n\n#"))
            .build();
    }
}
