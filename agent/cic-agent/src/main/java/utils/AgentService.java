package utils;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import entity.request.ConnectorRequest;
import entity.response.AgentConnectionInfo;
import entity.response.AgentConnectionOptions;
import entity.response.ConnectorInfo;
import entity.response.NexusAgentItem;
import entity.response.NexusAgentResponse;
import enums.CICAgentType;
import enums.NexusAPIEnum;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Data
@AllArgsConstructor
public class AgentService {
    private static String installExecutableFile;
    private static String webLoginSession;

    private Session jSchSession = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;
    private AgentCredentials agentCredentials;
    private NexusAgentItem nexusAgentItem = null;
    private AgentConnectionOptions agentConnectionOptions = null;
    private ConnectorInfo connectorInfo = null;
    private AgentData agentData;

    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    public AgentService() {
        agentCredentials = new AgentCredentials().getAgentCredentials();
        agentData = new AgentData();
    }

    /**
     * Create Session to connect to VM
     *
     * @return current class object
     */
    @SneakyThrows
    public AgentService createRemoteSession() {
        JSch jSch = new JSch();
        try {
            agentData.setPrivateKeyFile(agentData.getBaseFolder() + File.separator + "key" + File.separator +
                StringUtils.substringAfterLast(AgentConstants.AWS_SYSTEM_PARAMETER_PRIVATE_KEY, "/"));
            String privateKey = FileResourceUtil.getAwsSystemParameter(AgentConstants.AWS_SYSTEM_PARAMETER_PRIVATE_KEY);
            log.info("########## PRIVATE KEY RETRIEVED FROM AWS SUCCESSFULLY. ########  " + agentData.getPrivateKeyFile());
            FileUtils.writeStringToFile(new File(agentData.getPrivateKeyFile()), privateKey, StandardCharsets.UTF_8);
            jSch.addIdentity(agentData.getPrivateKeyFile(), agentCredentials.getPassword());
            log.info("########## PRIVATE KEY ADDED SUCCESSFULLY. ########");
            jSchSession = jSch.getSession(agentCredentials.getUsername(), agentCredentials.getHost(), Integer.parseInt(agentCredentials.getPort()));
            log.info("########## SESSION CREATED SUCCESSFULLY. ########");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            jSchSession.setConfig(config);
            jSchSession.connect(SESSION_TIMEOUT);
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
            channel = jSchSession.openChannel("sftp");
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
        jSchSession.disconnect();
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
            channelSftp.cd(AgentConstants.REMOTE_WC_INSTALL_FOLDER);
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
                execute(AgentConstants.REMOTE_WC_INSTALL_FOLDER + File.separator + executableFile + " -q");
            }
            if (recursiveFolderDelete(AgentConstants.REMOTE_WC_INSTALL_FOLDER)) {
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
        Map<String, String> paramMap = new HashMap<>();
        NexusAgentResponse nexusAgentResponse = null;
        String nexusRepository;
        String nexusVersion;
        RequestEntity requestEntity;
        try {
            nexusRepository = PropertiesContext.get("nexus_repository");
        } catch (IllegalArgumentException illegalArgumentException) {
            nexusRepository = PropertiesContext.get("ci-connect.nexus_repository");
        }
        try {
            nexusVersion = PropertiesContext.get("nexus_version").replace("/", ".");
        } catch (IllegalArgumentException illegalArgumentException) {
            nexusVersion = PropertiesContext.get("ci-connect.nexus_version");
        }
        String group = "/" + PropertiesContext.get("ci-connect.nexus_group") + "/" + nexusVersion;
        String credential = PropertiesContext.get("global.nexus.username") + ":" + PropertiesContext.get("global.nexus.password");
        try {
            requestEntity = RequestEntityUtil.init(NexusAPIEnum.NEXUS_CIC_AGENT_SEARCH_BY_GROUP, NexusAgentResponse.class)
                .inlineVariables(nexusRepository, group)
                .headers(new HashMap<String, String>() {
                    {
                        put("Authorization", "Basic " + Base64.getEncoder().encodeToString(credential.getBytes()));
                    }
                }).expectedResponseCode(HttpStatus.SC_OK);

            nexusAgentResponse = (NexusAgentResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage() + "REPOSITORY NOT FOUND IN NEXUS - " + group);
        }

        try {
            while (nexusAgentResponse.getContinuationToken() != null) {
                paramMap.put("continuationToken", nexusAgentResponse.getContinuationToken());
                requestEntity = RequestEntityUtil.init(NexusAPIEnum.NEXUS_CIC_AGENT_SEARCH_BY_GROUP, NexusAgentResponse.class)
                    .inlineVariables(nexusRepository, group)
                    .headers(new HashMap<String, String>() {
                        {
                            put("Authorization", "Basic " + Base64.getEncoder().encodeToString(credential.getBytes()));
                        }
                    }).expectedResponseCode(HttpStatus.SC_OK);
                nexusAgentResponse = (NexusAgentResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
            }

            nexusAgentItem = nexusAgentResponse.getItems().stream()
                .filter(wf -> wf.getName().endsWith(PropertiesContext.get("ci-connect.nexus_extension")))
                .findFirst()
                .get();

            String isRepositoryFound = (nexusAgentItem != null) ? "FOUND" : "NOT FOUND";
            log.info(String.format("AGENT REPOSITORY %s WITH %s IN NEXUS", isRepositoryFound, group));
        } catch (Exception e) {
            log.error("AGENT INSTALLER NOT FOUND IN NEXUS REPOSITORY!!");
        }

        return this;
    }

    /**
     * Download agent executable zip file from nexus repository to local folder
     *
     * @return current class object
     */
    public AgentService downloadAgentFile() {
        Boolean customInstall;
        try {
            agentData.setBaseFolder(String.valueOf(FileResourceUtil.createTempDir(null)).toLowerCase());
            agentData.setAgentZipFolder(agentData.getBaseFolder() + File.separator + StringUtils.substringAfterLast(nexusAgentItem.getName(), "/"));
            agentData.setAgentUnZipFolder(agentData.getBaseFolder() + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusAgentItem.getName(), "/")));
        } catch (Exception ioException) {
            log.error("PATH NOT FOUND!!");
        }

        try {
            customInstall = Boolean.valueOf(PropertiesContext.get("custom_install"));
        } catch (IllegalArgumentException illegalArgumentException) {
            customInstall = Boolean.valueOf(PropertiesContext.get("ci-connect.custom_install"));
        }
        if (customInstall) {
            NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL.setEndpoint(nexusAgentItem.getAssets().get(0).getDownloadUrl());
            downloadAgent(NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL);
        } else {
            String lastModifiedDate = nexusAgentItem.getAssets().get(0).getLastModified().split("T")[0];
            if (LocalDate.now().minusDays(8).isAfter(LocalDate.parse(lastModifiedDate))) {
                log.info("AGENT INSTALLER IS NOT UPDATED " + nexusAgentItem.getName());
                System.exit(0);
            } else {
                NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL.setEndpoint(nexusAgentItem.getAssets().get(0).getDownloadUrl());
                downloadAgent(NexusAPIEnum.NEXUS_CIC_AGENT_DOWNLOAD_URL);
            }
        }
        return this;
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     */
    public AgentService unZip() {
        ZipInputStream zipIn = null;
        try {
            File destDir = new File(agentData.getAgentUnZipFolder());
            if (!destDir.exists()) {
                destDir.mkdir();
            } else {
                FileUtils.cleanDirectory(destDir);
            }
            zipIn = new ZipInputStream(new FileInputStream(agentData.getAgentZipFolder()));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = agentData.getAgentUnZipFolder() + File.separator + entry.getName();
                log.info(String.format("########## DOWNLOADED AGENT FOLDER --%s ###############", filePath));
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException ioException) {
            log.error(String.valueOf(ioException), ioException);
        }
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
                        case "port=":
                            stringBuilder.append(line).append(agentConnectionOptions.getPort().toString()).append("\n");
                            break;
                        case "auth-token=":
                            stringBuilder.append(line).append(agentConnectionOptions.getAuthToken()).append("\n");
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
                        case "reconnectionInterval=":
                            stringBuilder.append(line).append(agentConnectionOptions.getReconnectionInterval().toString()).append("\n");
                            break;
                        case "agentId=":
                            stringBuilder.append(line).append(agentConnectionOptions.getAgentId()).append("\n");
                            break;
                        case "plmType=":
                            stringBuilder.append(line).append(agentConnectionOptions.getPlmType()).append("\n");
                            break;
                        case "hostName=":
                            stringBuilder.append(line).append(agentConnectionOptions.getHostName()).append(StringUtils.capitalize(agentConnectionOptions.getPlmType())).append("\n");
                            break;
                        case "user=":
                            stringBuilder.append(line).append(agentConnectionOptions.getPlmUser()).append("\n");
                            break;
                        case "password=":
                            stringBuilder.append(line).append(agentConnectionOptions.getPlmPassword()).append("\n");
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
        String connectorName;
        try {
            connectorName = PropertiesContext.get("connector");
        } catch (IllegalArgumentException illegalArgumentException) {
            connectorName = PropertiesContext.get("ci-connect." + PropertiesContext.get("agent_type") + ".connector");
        }
        connectorInfo = CicApiTestUtil.getMatchedConnector(connectorName, loginSession);
        if (null == connectorInfo) {
            switch (PropertiesContext.get("agent_type")) {
                case "windchill":
                    connectorRequestDataBuilder = CicApiTestUtil.getConnectorBaseData(CICAgentType.WINDCHILL);
                    connectorRequestDataBuilder.setDisplayName(connectorName);
                    ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateConnector(connectorRequestDataBuilder, loginSession);
                    if (responseWrapper.getBody().contains("true")) {
                        log.info("CREATED CONNECTOR WITH NAME - " + connectorName);
                    }
            }
            connectorInfo = CicApiTestUtil.getMatchedConnector(connectorName, loginSession);
        } else {
            log.info("FOUND CONNECTOR WITH NAME - " + connectorName);
        }
        return this;
    }

    /**
     * Get connector options information.
     *
     * @return current class object
     */
    public AgentService getConnectorOptions() {
        AgentConnectionInfo agentConnectionInfo = null;
        Integer portNumber;
        try {
            portNumber = Integer.valueOf(PropertiesContext.get("port"));
        } catch (IllegalArgumentException illegalArgumentException) {
            portNumber = Integer.valueOf(PropertiesContext.get("ci-connect." + PropertiesContext.get("agent_type") + ".port"));
        }
        switch (PropertiesContext.get("agent_type")) {
            case "windchill":
                agentConnectionInfo = CicApiTestUtil.getAgentConnectorOptions(connectorInfo.getName(), webLoginSession);
                agentConnectionOptions = AgentConnectionOptions.builder()
                    .agentName(connectorInfo.getName())
                    .appKey(agentConnectionInfo.getAppKey())
                    .wssUrl(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "url=", "\n\n#"))
                    .scanRate(Integer.valueOf(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "scanRate=", "\n\n#")))
                    .plmType(StringUtils.substringAfter(agentConnectionInfo.getConnectionInfo(), "plmType=").replace(")", ""))
                    .agentId(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "agentId=", "\n\n#"))
                    .reconnectionInterval(3)
                    .authToken("DogCatMonkey")
                    .port(portNumber)
                    .installDirectory("C:" + AgentConstants.REMOTE_WC_INSTALL_FOLDER)
                    .plmUser(agentCredentials.getPlmUser())
                    .plmPassword(agentCredentials.getPlmPassword())
                    .hostName("https://consvwc02.apriori.com/")
                    .build();
        }
        return this;
    }

    /**
     * Install certificates after installation of agent
     *
     * @return current class object
     */
    public AgentService installCertificates() {
        String jreBinDirectory = AgentConstants.REMOTE_WC_INSTALL_FOLDER + File.separator + "jre/bin/";
        try {
            channelSftp.cd(jreBinDirectory);
            execute(jreBinDirectory + "keytool -importcert -file " + AgentConstants.REMOTE_CERTIFICATE_FOLDER + "/fbc-consvdc02-CA-2026.cer -alias fbc-consvdc02-2026 -keystore \"" + AgentConstants.REMOTE_WC_INSTALL_FOLDER + "/jre/jre/lib/security/cacerts\" -storepass changeit -noprompt");
            execute(jreBinDirectory + "keytool -importcert -file " + AgentConstants.REMOTE_CERTIFICATE_FOLDER + "/fbc1-2040.cer -alias fbc1-2040 -keystore \"" + AgentConstants.REMOTE_WC_INSTALL_FOLDER + "/jre/jre/lib/security/cacerts\" -storepass changeit -noprompt");
        } catch (Exception e) {
            log.error("FAILED TO IMPORT CERTIFICATES");
        }
        return this;
    }

    /**
     * Get connector status information to verify its connected to PLM
     *
     * @return ConnectorInfo class object
     */
    public ConnectorInfo getConnectorStatusInfo() {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(5);
        String connectorName;
        try {
            connectorName = PropertiesContext.get("connector");
        } catch (IllegalArgumentException illegalArgumentException) {
            connectorName = PropertiesContext.get("ci-connect." + PropertiesContext.get("agent_type") + ".connector");
        }
        ConnectorInfo connectorInfo = CicApiTestUtil.getMatchedConnector(connectorName, webLoginSession);
        try {
            while (!(connectorInfo.getConnectionStatus().equals("Connected to PLM"))) {
                if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                    break;
                }
                TimeUnit.SECONDS.sleep(30);
                connectorInfo = CicApiTestUtil.getMatchedConnector(connectorName, webLoginSession);
            }
            String isConnected = (connectorInfo.getConnectionStatus().equals("Connected to PLM")) ? "CONNECTED" : "NOT CONNECTED";
            log.info(String.format("CONNECTOR (%s) TO PLM ---%s", isConnected, connectorInfo.getDisplayName()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return connectorInfo;
    }

    /**
     * Execute coommand on remote VM machine using JSch open channel sftp connection
     *
     * @param command command to execute
     */
    private void execute(String command) {
        int exitStatus = -1;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            channel = (Channel) jSchSession.openChannel("exec");
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
            log.info(stringBuilder.toString());
            log.info(String.format("Exit status of the execution: %s ", (exitStatus == 0) ? exitStatus + "(FINISHED EXECUTION)" : exitStatus + "(EXECUTION FAILED)"));
        } catch (JSchException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Download agent from nexus download api url
     *
     * @param agentUrl NexusAPIEnum
     */
    private void downloadAgent(NexusAPIEnum agentUrl) {
        InputStream inputStream = null;
        try {
            File agentZipFile = new File(agentData.getAgentZipFolder());
            if (agentZipFile.exists()) {
                agentZipFile.delete();
            }
            String userCredentials = PropertiesContext.get("global.nexus.username") + ":" + PropertiesContext.get("global.nexus.password");
            inputStream = RestAssured.given()
                .headers(new HashMap<String, String>() {
                    {
                        put("Authorization", "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes()));
                    }
                })
                .config(RestAssuredConfig.config()
                    .httpClient(
                        HttpClientConfig.httpClientConfig()
                            .setParam("http.connection.timeout", 60000)
                            .setParam("http.socket.timeout", 60000)
                    ))
                .get(agentUrl.NEXUS_CIC_AGENT_DOWNLOAD_URL.getEndpointString())
                .asInputStream();

            FileUtils.copyInputStreamToFile(inputStream, new File(agentData.getAgentZipFolder()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error("FAILED TO DOWNLOAD THE AGENT INSTALLER!!!!");
        }
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     */
    private void extractFile(ZipInputStream zipIn, String filePath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (IOException ioException) {
            log.error(String.valueOf(ioException), ioException);
        }
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
            throw new IllegalArgumentException(String.format(
                "Could not able to connect SFTP connection and not able to find directory with name '%s'",
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
}
