package com.apriori.cic.api.agent;

import com.apriori.cic.api.utils.AgentConstants;
import com.apriori.cic.api.utils.AgentCredentials;
import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.nexus.utils.NexusComponent;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Vector;

@Slf4j
public class FtpClient {
    private Session jschSession = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;

    private final AgentCredentials agentCredentials;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    public FtpClient() {
        agentCredentials = new AgentCredentials().getAgentCredentials();
        createRemoteSession();
        getSftpConnection();
    }

    /**
     * Create Session to connect to VM
     */
    @SneakyThrows
    private void createRemoteSession() {
        JSch jsch = new JSch();
        try {
            String keyFolder = String.valueOf(FileResourceUtil.createTempDir(null)).toLowerCase();
            String awsPrivateKeyFile = keyFolder + File.separator + "key" + File.separator +
                StringUtils.substringAfterLast(AgentConstants.AWS_SYSTEM_PARAMETER_PRIVATE_KEY, "/");
            String privateKey = AwsParameterStoreUtil.getSystemParameter(AgentConstants.AWS_SYSTEM_PARAMETER_PRIVATE_KEY);
            log.info("########## PRIVATE KEY RETRIEVED FROM AWS SUCCESSFULLY. ########  " + awsPrivateKeyFile);
            FileUtils.writeStringToFile(new File(awsPrivateKeyFile), privateKey, StandardCharsets.UTF_8);
            jsch.addIdentity(awsPrivateKeyFile, agentCredentials.getPassword());
            log.debug("########## PRIVATE KEY ADDED SUCCESSFULLY. ########");
            jschSession = jsch.getSession(agentCredentials.getUsername(), agentCredentials.getHost(), Integer.parseInt(agentCredentials.getPort()));
            log.debug("########## SESSION CREATED SUCCESSFULLY. ########");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);
            jschSession.connect(SESSION_TIMEOUT);
        } catch (Exception connectionException) {
            throw new Exception("SESSION CONNECTION FAILED!!" + connectionException.getMessage());
        }
    }

    /**
     * Create and SFTP connection to VM to delete the folder and upload the files.
     */
    private void getSftpConnection() {
        try {
            channel = jschSession.openChannel("sftp");
            channel.connect(CHANNEL_TIMEOUT);
            log.info("########## CONNECTED TO VM SUCCESSFULLY. ########");
            channelSftp = (ChannelSftp) channel;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    public FtpClient uploadAndInstall(NexusComponent nexusComponent) {
        String remoteDownloadFolder = AgentConstants.REMOTE_DOWNLOAD_FOLDER + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusComponent.getAgentItemName(), "/"));
        try {
            recursiveFolderDelete(AgentConstants.REMOTE_DOWNLOAD_FOLDER + File.separator + FilenameUtils.removeExtension(StringUtils.substringAfterLast(nexusComponent.getAgentItemName(), "/")));
            recursiveFolderUpload(nexusComponent.getAgentUnZipFolder(), AgentConstants.REMOTE_DOWNLOAD_FOLDER);
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
     * Restart Agent Service
     *
     * @return current class object
     */
    public FtpClient executeAgentService(String agentInstallFolder) {
        runService(agentInstallFolder, "stop");
        runService(agentInstallFolder, "start");
        return this;
    }

    /**
     * get list of files from remote folder
     *
     * @param remoteFolderToSearch - remoteFolder
     * @return List<Vector < ChannelSftp.LsEntry>
     */
    public Vector<ChannelSftp.LsEntry> getFilesList(String remoteFolderToSearch) {
        Vector<ChannelSftp.LsEntry> remoteFileList;
        try {
            channelSftp.cd(remoteFolderToSearch);
            remoteFileList = channelSftp.ls(remoteFolderToSearch);
        } catch (SftpException e) {
            throw new IllegalArgumentException("REPORTS FOLDER NOT FOUND!!! " + e.getMessage());
        }
        return remoteFileList;
    }

    /**
     * get matched Remote folder
     *
     * @param baseFolder            - base Folder
     * @param searchForRemoteFolder - folder to search
     * @return matched folder if it exists
     */
    public String getMatchedFolder(String baseFolder, String searchForRemoteFolder) {
        String remoteWorkflowJobFolder;
        try {
            channelSftp.cd(baseFolder);
            remoteWorkflowJobFolder = getFilesList(baseFolder)
                .stream()
                .filter(item -> item.getFilename().contains(searchForRemoteFolder))
                .map(ChannelSftp.LsEntry::getFilename)
                .findFirst()
                .orElse(null);
        } catch (SftpException e) {
            throw new IllegalArgumentException("REPORTS FOLDER NOT FOUND!!! " + e.getMessage());
        }
        return remoteWorkflowJobFolder;
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
     * Delete file and folder on remote VM
     *
     * @param path folder to be deleted
     * @return boolean (true or false) indicates succesfull deletion.
     */
    public Boolean recursiveFolderDelete(String path) {
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
    public void recursiveFolderUpload(String sourcePath, String destinationPath) {
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
                    } catch (SftpException e) {
                        log.error(destinationPath + "/" + sourceFile.getName() + " not found");
                    }
                    // else create a directory
                    if (attrs == null || !attrs.isDir()) {
                        log.info("Creating dir " + sourceFile.getName());
                        channelSftp.mkdir(sourceFile.getName());
                    }
                    for (File f : files) {
                        recursiveFolderUpload(f.getAbsolutePath(), destinationPath + "/" + sourceFile.getName());
                    }
                }
            }
        } catch (SftpException | FileNotFoundException e) {
            throw new IllegalArgumentException(String.format(
                "DESTINATION FOLDER (%s) ON REMOTE SERVER NOT FOUND!!",
                destinationPath));
        }
    }

    public void uploadFileToAgent(String sourcePath, String destinationPath) {
        try {
            File sourceFile = new File(sourcePath);
            String inputFileName = "cig-input.xlsx";
            channelSftp.cd(destinationPath);
            if (!sourceFile.getName().startsWith(".")) {
                channelSftp.put(new FileInputStream(sourceFile), inputFileName, ChannelSftp.OVERWRITE);
            }
        } catch (SftpException | FileNotFoundException e) {
            throw new IllegalArgumentException(String.format(
                "DESTINATION FOLDER (%s) ON REMOTE SERVER NOT FOUND!!",
                destinationPath));
        }
    }

    /**
     * Uninstall the agent from VM and delete all the folders
     *
     * @return current class object
     */
    public void UnInstall(String installFolder) {
        String executableFile = StringUtils.EMPTY;
        try {
            channelSftp.cd(installFolder);
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
                execute(installFolder + File.separator + executableFile + " -q");
            }
            if (recursiveFolderDelete(installFolder)) {
                log.info("AGENT UNINSTALLED AND DELETED THE FOLDER SUCCESSFULLY!!!!");
            }
        } catch (SftpException sftpException) {
            log.error(sftpException.getMessage());
        }
    }

    /**
     * Install certificates after installation of agent
     * Amazing i
     */
    public FtpClient installCertificates(String installFolder) {
        String jreBinDirectory = StringUtils.EMPTY;
        jreBinDirectory = installFolder + File.separator + "jre/bin/";
        try {
            channelSftp.cd(jreBinDirectory);
            execute(jreBinDirectory + "keytool -importcert -file " + AgentConstants.REMOTE_CERTIFICATE_FOLDER + "/fbc-consvdc02-CA-2026.cer -alias fbc-consvdc02-2026 -keystore \"" + installFolder + "/jre/lib/security/cacerts\" -storepass changeit -noprompt");
            execute(jreBinDirectory + "keytool -importcert -file " + AgentConstants.REMOTE_CERTIFICATE_FOLDER + "/fbc1-2040.cer -alias fbc1-2040 -keystore \"" + installFolder + "/jre/lib/security/cacerts\" -storepass changeit -noprompt");
        } catch (Exception e) {
            throw new IllegalArgumentException("FAILED TO IMPORT CERTIFICATES " + e);
        }
        return this;
    }
}