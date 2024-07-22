package com.apriori.cic.api.agent;

import com.apriori.cic.api.enums.AgentOptionFields;
import com.apriori.cic.api.utils.AgentConstants;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.ExcelService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.nexus.utils.NexusComponent;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
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
     * upload options file to remote agent workflow folder
     *
     * @param workflowName  - Workflow name
     * @param inputFileName - options file to be copied
     */
    @SneakyThrows
    public void uploadInputFileToRemoteWorkflowFolder(String workflowName, String inputFileName) {
        try {
            String workflowFolder = String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"), workflowName);
            File testDataInputFile = FileResourceUtil.getLocalResourceFile("testdata/" + inputFileName + ".xlsx");
            String destinationFolder = workflowFolder + File.separator + "inputs";
            String cadFilesFolder = destinationFolder + File.separator + "cad-files";
            ftpClient.uploadFileToAgent(String.valueOf(testDataInputFile), destinationFolder);
            List<PartData> partDataList = getInputFileData(inputFileName);
            partDataList.stream().forEach(partData -> {
                File cloudFile = getCloudFileAndSaveWithName(partData.getPartName().split("auto_")[1], ProcessGroupEnum.fromString(partData.getProcessGroup()), partData.getPartName());
                ftpClient.recursiveFolderUpload(String.valueOf(cloudFile), cadFilesFolder);
            });
        } catch (Exception e) {
            throw new RuntimeException("FAILED TO UPLOAD INPUT FILES AND CAD-FILES TO AGENT FILE SYSTEM WORKFLOW FOLDER!!!!");
        }
    }

    /**
     * verify report data
     *
     * @param inputFile    - expected data file
     * @param workflowName - workflow name
     * @param jobId        - workflow job id
     * @return - true or false
     */
    public Boolean verifyReports(String inputFile, String workflowName, String jobId) {
        String outputFolder = getWorkflowOutputFolder(workflowName, jobId);
        List<PartData> partDataList = getInputFileData(inputFile);
        try {
            String workflowFolder = String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"), workflowName);
            String reportsFolder = workflowFolder + File.separator + "outputs" + File.separator + outputFolder + File.separator + "reports";
            return ftpClient.getFilesList(reportsFolder).stream()
                .anyMatch(file ->
                    partDataList.stream()
                        .peek(partData -> {
                            if (!file.getFilename().contains(partData.getPartName().split("\\.")[0])) {
                                log.debug(String.format("ACTUAL Document content : (%s) <=> EXPECTED PART NAME : (%s)", file.getFilename(), partData.getPartName()));
                            }
                        })
                        .anyMatch(partData ->
                            file.getFilename().contains(partData.getPartName().split("\\.")[0])
                        )
                );
        } catch (Exception e) {
            throw new IllegalArgumentException("REPORTS FOLDER NOT FOUND!!!");
        }
    }

    /**
     * verify workflow output file
     *
     * @param workflowName - workflow name
     * @param jobId        - workflow job id
     * @return - true or false
     */
    public Boolean verifyOutputFile(String workflowName, String jobId) {
        String outputFolder = getWorkflowOutputFolder(workflowName, jobId);
        String workflowFolder;
        Boolean isOutFileExists = false;
        try {
            workflowFolder = String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"), workflowName);
            String destinationFolder = workflowFolder + File.separator + "outputs" + File.separator + outputFolder;
            isOutFileExists = ftpClient.getFilesList(destinationFolder).stream().filter(file -> file.getFilename().equals("cig-output.xlsx"))
                .findFirst().isPresent();
        } catch (Exception e) {
            throw new IllegalArgumentException("cig-output.xlsx file NOT FOUND!!!");
        }
        return isOutFileExists;
    }

    /**
     * delete workflow folder from agent VM
     *
     * @param workflowName - workflow name
     * @return true or false
     */
    public Boolean deleteWorkflowFromAgent(String workflowName) {
        return ftpClient.recursiveFolderDelete(String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"), workflowName));
    }

    /**
     * de-serialize input file to Part Data object
     *
     * @param inputFileName - input file name
     * @return - PartData list
     */
    private List<PartData> getInputFileData(String inputFileName) {
        List<PartData> partDataList = new ArrayList<>();
        PartData partData;
        try {
            ExcelService excelService = new ExcelService(String.valueOf(FileResourceUtil.getLocalResourceFile("testdata/" + inputFileName + ".xlsx")), "Sheet0");
            for (int i = 2; i <= excelService.getRowCount(); i++) {
                partData = new PartData();
                partData.setPartName(excelService.getCellData("CAD File Name", i));
                partData.setProcessGroup(excelService.getCellData("Process Group", i));
                partDataList.add(partData);
            }
        } catch (Exception e) {
            throw new RuntimeException("FAILED TO RETRIEVE INPUT FILE DATA!!");
        }
        return partDataList;
    }

    /**
     * get workflow output folder from Agent
     * @param workflowName - workflow name
     * @param jobId - job id
     * @return - folder name
     */
    private String getWorkflowOutputFolder(String workflowName, String jobId) {
        String workflowFolder = StringUtils.EMPTY;
        String outputFolder = StringUtils.EMPTY;
        try {
            workflowFolder = String.format(AgentConstants.REMOTE_FS_ROOT_FOLDER, PropertiesContext.get("env"), PropertiesContext.get("customer"), workflowName);
            String destinationFolder = workflowFolder + File.separator + "outputs";
            String searchForRemoteFolder = jobId.split("-")[jobId.split("-").length - 1];
            return ftpClient.getMatchedFolder(destinationFolder, searchForRemoteFolder);
        } catch (Exception e) {
            log.warn(String.format(
                "Either directory >> %s << does not exists or already deleted from remote agent VM",
                workflowFolder
            ));
        }
        return outputFolder;
    }

    /**
     * get file name from s3 bucket
     * @param s3ComponentName - component name
     * @param processGroup - ProcessGroupEnum
     * @param newFileName - file name to be saved
     * @return File
     */
    @SneakyThrows
    public static File getCloudFileAndSaveWithName(String s3ComponentName, ProcessGroupEnum processGroup, String newFileName) {
        File tempFile = FileResourceUtil.getCloudFile(processGroup, s3ComponentName);
        File newFile = new File(tempFile.getParent(), newFileName);
        Files.move(tempFile.toPath(), newFile.toPath());
        return newFile;
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