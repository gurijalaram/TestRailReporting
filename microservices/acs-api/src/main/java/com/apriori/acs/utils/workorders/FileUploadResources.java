package com.apriori.acs.utils.workorders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.enums.workorders.CidWorkorderApiEnum;
import com.apriori.acs.entity.enums.workorders.WorkorderStatusEnum;
import com.apriori.acs.entity.request.workorders.cost.createcostworkorder.CostOrderInputs;
import com.apriori.acs.entity.request.workorders.cost.createcostworkorder.CostOrderScenario;
import com.apriori.acs.entity.request.workorders.cost.createcostworkorder.CostOrderScenarioIteration;
import com.apriori.acs.entity.request.workorders.cost.productioninfo.ProductionInfo;
import com.apriori.acs.entity.request.workorders.cost.productioninfo.ProductionInfoMaterial;
import com.apriori.acs.entity.request.workorders.cost.productioninfo.ProductionInfoScenario;
import com.apriori.acs.entity.request.workorders.cost.productioninfo.ProductionInfoScenarioKey;
import com.apriori.acs.entity.request.workorders.cost.productioninfo.ProductionInfoVpe;
import com.apriori.acs.entity.request.workorders.publish.createpublishworkorder.PublishInputs;
import com.apriori.acs.entity.request.workorders.publish.createpublishworkorder.PublishScenarioIterationKey;
import com.apriori.acs.entity.request.workorders.publish.createpublishworkorder.PublishScenarioKey;
import com.apriori.acs.entity.response.workorders.genericclasses.CreateWorkorderResponse;
import com.apriori.acs.entity.response.workorders.getadmininfo.GetAdminInfoResponse;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.GetCadMetadataResponse;
import com.apriori.acs.entity.response.workorders.getimageinfo.GetImageInfoResponse;
import com.apriori.acs.entity.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.entity.response.workorders.cost.iterations.CostIteration;
import com.apriori.acs.entity.response.workorders.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.acs.entity.response.workorders.deletescenario.DeleteScenarioInputs;
import com.apriori.acs.entity.response.workorders.deletescenario.DeleteScenarioOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileResponse;
import com.apriori.acs.entity.response.workorders.upload.FileUploadInputs;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.entity.response.workorders.upload.FileWorkorder;
import com.apriori.acs.entity.response.workorders.generateassemblyimages.GenerateAssemblyImagesInputs;
import com.apriori.acs.entity.response.workorders.generateassemblyimages.GenerateAssemblyImagesOutputs;
import com.apriori.acs.entity.response.workorders.generatepartimages.GeneratePartImagesInputs;
import com.apriori.acs.entity.response.workorders.generatepartimages.GeneratePartImagesOutputs;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.LoadCadMetadataInputs;
import com.apriori.acs.entity.response.workorders.loadcadmetadata.LoadCadMetadataOutputs;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioKey;
import com.apriori.acs.entity.response.workorders.genericclasses.WorkorderCommand;
import com.apriori.acs.entity.response.workorders.genericclasses.WorkorderCommands;
import com.apriori.acs.entity.response.workorders.genericclasses.WorkorderDetailsResponse;
import com.apriori.acs.entity.response.workorders.genericclasses.WorkorderRequest;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;
import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.UrlEscapers;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);
    private static final long WAIT_TIME = 180;

    private static final HashMap<String, String> token = new APIAuthentication()
        .initAuthorizationHeaderNoContent(UserUtil.getUser().getEmail());

    private static final HashMap<String, String> headers = new HashMap<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ArrayList<LoadCadMetadataOutputs> componentMetadataOutputs = new ArrayList<>();

    private FileUploadOutputs currentFileUploadOutputs;

    /**
     * Uploads part to CID
     *
     * @param fileName     - name of file to upload
     * @param processGroup - process group of file
     * @return FileResponse - response to use in next call
     */
    public FileResponse initialisePartUpload(String fileName, String processGroup) {
        return initializeFileUpload(fileName, processGroup);
    }

    /**
     * Upload part, suppress 500 error (retry file upload three times)
     *
     * @param fileResponse response from file upload initialise
     * @param scenarioName scenario name to use
     * @return FileUploadOutputs instance
     */
    public FileUploadOutputs createFileUploadWorkorderSuppressError(FileResponse fileResponse, String scenarioName) {
        String fileUploadWorkorderId = createFileUploadWorkorder(WorkorderCommands.LOAD_CAD_FILE.getWorkorderCommand(),
            FileUploadInputs.builder()
                .keepFreeBodies(false)
                .freeBodiesPreserveCad(false)
                .freeBodiesIgnoreMissingComponents(true)
                .scenarioName(scenarioName)
                .fileKey(fileResponse.getIdentity())
                .fileName(fileResponse.getFilename())
                .build(),
            true
        );
        submitWorkorder(fileUploadWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(fileUploadWorkorderId),
            FileUploadOutputs.class
        );
    }

    /**
     * Upload part, expose 500 error (only one file upload attempt)
     *
     * @param fileResponse response from file upload initialise
     * @param scenarioName scenario name to use
     * @return FileUploadOutputs instance
     */
    public FileUploadOutputs createFileUploadWorkorderExposeError(FileResponse fileResponse, String scenarioName) {
        String fileUploadWorkorderId = createFileUploadWorkorder(WorkorderCommands.LOAD_CAD_FILE.getWorkorderCommand(),
            FileUploadInputs.builder()
                .keepFreeBodies(false)
                .freeBodiesPreserveCad(false)
                .freeBodiesIgnoreMissingComponents(true)
                .scenarioName(scenarioName)
                .fileKey(fileResponse.getIdentity())
                .fileName(fileResponse.getFilename())
                .build(),
            false
        );
        submitWorkorder(fileUploadWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(fileUploadWorkorderId),
            FileUploadOutputs.class
        );
    }

    /**
     * Loads CAD Metadata (suppressing 500 error)
     *
     * @param fileResponse - response from file upload
     * @return LoadCadMetadataOutputs - outputs to use in next call
     */
    public LoadCadMetadataOutputs loadCadMetadataSuppressError(FileResponse fileResponse) {
        String loadCadMetadataWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_METADATA.getWorkorderCommand(),
            LoadCadMetadataInputs.builder()
                .keepFreeBodies(false)
                .freeBodiesPreserveCad(false)
                .freeBodiesIgnoreMissingComponents(true)
                .fileMetadataIdentity(fileResponse.getIdentity())
                .requestedBy(fileResponse.getUserIdentity())
                .fileName(fileResponse.getFilename())
                .build(),
            true
        );
        submitWorkorder(loadCadMetadataWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(loadCadMetadataWorkorderId),
            LoadCadMetadataOutputs.class
        );
    }

    /**
     * Loads CAD Metadata (exposing 500 error)
     *
     * @param fileResponse - response from file upload
     * @return LoadCadMetadataOutputs - outputs to use in next call
     */
    public LoadCadMetadataOutputs loadCadMetadataExposeError(FileResponse fileResponse) {
        String loadCadMetadataWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_METADATA.getWorkorderCommand(),
            LoadCadMetadataInputs.builder()
                .keepFreeBodies(false)
                .freeBodiesPreserveCad(false)
                .freeBodiesIgnoreMissingComponents(true)
                .fileMetadataIdentity(fileResponse.getIdentity())
                .requestedBy(fileResponse.getUserIdentity())
                .fileName(fileResponse.getFilename())
                .build(),
            false
        );
        submitWorkorder(loadCadMetadataWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(loadCadMetadataWorkorderId),
            LoadCadMetadataOutputs.class
        );
    }

    /**
     * Generates part images
     *
     * @param fileResponse           - response from file upload
     * @param loadCadMetadataOutputs - output from load cad metadata
     * @return GeneratePartImagesOutputs - response to use in next call
     */
    public GeneratePartImagesOutputs generatePartImages(FileResponse fileResponse,
                                                        LoadCadMetadataOutputs loadCadMetadataOutputs) {
        String generatePartImagesWorkorderId = createWorkorder(
            WorkorderCommands.GENERATE_PART_IMAGES.getWorkorderCommand(),
            GeneratePartImagesInputs.builder()
                .cadMetadataIdentity(loadCadMetadataOutputs.getCadMetadataIdentity())
                .requestedBy(fileResponse.getUserIdentity())
                .build(),
            false
        );
        submitWorkorder(generatePartImagesWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(generatePartImagesWorkorderId),
            GeneratePartImagesOutputs.class
        );
    }

    /**
     * Generates assembly images for an assembly of any size
     *
     * @param fileResponse           - response from assembly file upload
     * @param loadCadMetadataOutputs - array list of assembly load cad metadata responses (assembly must be last item)
     * @param assemblyMetadataOutput - output from loading cad metadata for assembly
     * @return GenerateAssemblyImagesOutputs
     */
    public GenerateAssemblyImagesOutputs generateAssemblyImages(
        FileResponse fileResponse,
        ArrayList<LoadCadMetadataOutputs> loadCadMetadataOutputs,
        LoadCadMetadataOutputs assemblyMetadataOutput) {

        GenerateStringUtil generateStringUtil = new GenerateStringUtil();

        List<GenerateAssemblyImagesInputs> subComponentsList = new ArrayList<>();

        for (LoadCadMetadataOutputs loadCadMetadataOutput : loadCadMetadataOutputs) {
            subComponentsList.add(
                GenerateAssemblyImagesInputs.builder()
                    .componentIdentity(generateStringUtil.getRandomString())
                    .scenarioIdentity(generateStringUtil.getRandomString())
                    .cadMetadataIdentity(loadCadMetadataOutput.getCadMetadataIdentity())
                    .build()
            );
        }

        String generateAssemblyImagesWorkorderId = createWorkorder(
            WorkorderCommands.GENERATE_ASSEMBLY_IMAGES.getWorkorderCommand(),
            GenerateAssemblyImagesInputs.builder()
                .componentIdentity(generateStringUtil.getRandomString())
                .scenarioIdentity(generateStringUtil.getRandomString())
                .cadMetadataIdentity(assemblyMetadataOutput.getCadMetadataIdentity())
                .subComponents(subComponentsList)
                .requestedBy(fileResponse.getUserIdentity())
                .build(),
            false
        );
        submitWorkorder(generateAssemblyImagesWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(generateAssemblyImagesWorkorderId),
            GenerateAssemblyImagesOutputs.class
        );
    }

    /**
     * Cost uploaded part
     *
     * @param productionInfoInputs - production info inputs
     * @param fileUploadOutputs    - output from file upload
     * @param processGroup         - process group
     * @return CostOrderStatusOutputs
     */
    public CostOrderStatusOutputs costPart(Object productionInfoInputs, FileUploadOutputs fileUploadOutputs, String processGroup) {
        int inputSetId = initializeCostScenario(
            productionInfoInputs,
            fileUploadOutputs.getScenarioIterationKey().getScenarioKey(),
            processGroup
        );

        String costWorkorderId = createWorkorder(
            WorkorderCommands.COSTING.getWorkorderCommand(),
            CostOrderInputs.builder()
                .keepFreeBodies(false)
                .freeBodiesPreserveCad(false)
                .freeBodiesIgnoreMissingComponents(true)
                .inputSetId(inputSetId)
                .scenarioIterationKey(
                    setCostOrderScenarioIteration(
                        fileUploadOutputs.getScenarioIterationKey().getScenarioKey()))
                .build(),
            false
        );
        submitWorkorder(costWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(costWorkorderId),
            CostOrderStatusOutputs.class
        );
    }

    /**
     * Publish part
     *
     * @param costOutputs - outputs from cost
     * @return PublishResultOutputs - outputs from publish
     */
    public PublishResultOutputs publishPart(CostOrderStatusOutputs costOutputs) {
        String createPublishWorkorderId = createWorkorder(WorkorderCommands.PUBLISH.getWorkorderCommand(),
            PublishInputs.builder()
                .comments("Comments go here...")
                .description("Description goes here...")
                .scenarioIterationKey(
                    setPublishScenarioIterationKey(costOutputs.getScenarioIterationKey().getScenarioKey()))
                .build(),
            false
        );
        submitWorkorder(createPublishWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(createPublishWorkorderId),
            PublishResultOutputs.class
        );
    }

    /**
     * Validates image of any type
     *
     * @param imageResponse - image to validate
     */
    public void imageValidation(String imageResponse) {
        assertThat(Base64.isBase64(imageResponse), is(equalTo(true)));
    }

    /**
     * Create delete scenario workorder
     *
     * @param fileUploadOutputs - FileUploadOutputs - for use in building request
     * @return DeleteScenarioOutputs instance
     */
    public DeleteScenarioOutputs createDeleteScenarioWorkorderSuppressError(FileUploadOutputs fileUploadOutputs) {
        String deleteScenarioWorkorderId = createWorkorder(WorkorderCommands.DELETE.getWorkorderCommand(),
            DeleteScenarioInputs.builder()
                .scenarioIterationKey(fileUploadOutputs.getScenarioIterationKey())
                .iteration(fileUploadOutputs.getScenarioIterationKey().getIteration())
                .includeOtherWorkspace(false)
                .build(),
            true
        );
        submitWorkorder(deleteScenarioWorkorderId);
        return objectMapper.convertValue(
            checkGetWorkorderDetails(deleteScenarioWorkorderId),
            DeleteScenarioOutputs.class
        );
    }

    /**
     * Initializes file upload
     *
     * @param fileName     - the filename
     * @param processGroup - the process group
     * @return FileResponse
     */
    private FileResponse initializeFileUpload(String fileName, String processGroup) {
        setupHeaders("multipart/form-data");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.INITIALISE_FILE_UPLOAD, FileResponse.class)
            .headers(headers)
            .multiPartFiles(new MultiPartFiles().use("data",
                FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup), fileName)))
            .formParams(new FormParams().use("filename", fileName));

        assertThat(HTTPRequest.build(requestEntity).post().getStatusCode(), is(equalTo(201)));
        return (FileResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Gets Admin Info
     *
     * @param publishScenarioKey - Scenario Key from publish action
     * @return GetAdminInfoResponse object
     */
    public GetAdminInfoResponse getAdminInfo(ScenarioKey publishScenarioKey) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.GET_ADMIN_INFO, GetAdminInfoResponse.class)
            .headers(headers)
            .inlineVariables(
                publishScenarioKey.getWorkspaceId().toString(),
                publishScenarioKey.getTypeName(),
                publishScenarioKey.getMasterName(),
                publishScenarioKey.getStateName());

        return (GetAdminInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets image info
     *
     * @param scenarioIterationKey - Scenario Iteration Key from previous call
     * @return GetImageInfoResponse - json response from API call
     */
    public GetImageInfoResponse getImageInfo(ScenarioIterationKey scenarioIterationKey) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.GET_IMAGE_INFO, GetImageInfoResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString());

        return (GetImageInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get CAD Metadata
     *
     * @param fileMetadataIdentity - String of file metadata identity
     * @return GetCadMetadataResponse
     */
    public GetCadMetadataResponse getCadMetadata(String fileMetadataIdentity) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.GET_CAD_METADATA, GetCadMetadataResponse.class)
            .headers(headers)
            .inlineVariables(fileMetadataIdentity);

        return (GetCadMetadataResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Creates file upload workorder (with ignore HTTP 500 error capability)
     *
     * @param commandType    String
     * @param inputs         Object
     * @param ignore500Error Boolean
     * @return String file upload workorder id
     */
    private String createFileUploadWorkorder(String commandType, Object inputs, Boolean ignore500Error) {
        setupHeaders("application/json");

        RequestEntity requestEntity;
        if (ignore500Error) {
            requestEntity = RequestEntityUtil
                .init(CidWorkorderApiEnum.CREATE_WORKORDER, null)
                .headers(headers)
                .body(new WorkorderRequest()
                    .setCommand(new WorkorderCommand(
                        commandType,
                        inputs))
                );
            int counter = 0;
            while (HTTPRequest.build(requestEntity).post().getStatusCode() == 500 && counter < 2) {
                counter++;
            }
        }

        requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.CREATE_WORKORDER, CreateWorkorderResponse.class)
            .headers(headers)
            .body(new WorkorderRequest()
                .setCommand(new WorkorderCommand(
                    commandType,
                    inputs))
            );

        return jsonNode(HTTPRequest.build(requestEntity).post().getBody(), "id");
    }

    /**
     * Creates workorder (with ignore HTTP 500 error capability)
     *
     * @param commandType String
     * @param inputs      Object
     * @return String file upload workorder id
     */
    public String createWorkorder(String commandType, Object inputs, boolean ignore500Error) {
        setupHeaders("application/json");

        RequestEntity requestEntity;
        if (ignore500Error) {
            requestEntity = RequestEntityUtil
                .init(CidWorkorderApiEnum.CREATE_WORKORDER, null)
                .headers(headers)
                .body(new WorkorderRequest()
                    .setCommand(new WorkorderCommand(
                        commandType,
                        inputs))
                );
            int counter = 0;
            while (HTTPRequest.build(requestEntity).post().getStatusCode() == 500 && counter < 2) {
                counter++;
            }
        }

        requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.CREATE_WORKORDER, CreateWorkorderResponse.class)
            .headers(headers)
            .body(new WorkorderRequest()
                .setCommand(new WorkorderCommand(
                    commandType,
                    inputs))
            );

        return jsonNode(HTTPRequest.build(requestEntity).post().getBody(), "id");
    }

    /**
     * Checks if workorder processing has successfully completed
     *
     * @param workorderId - workorder id
     * @return Object
     */
    private Object getWorkorderDetails(String workorderId) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.GET_WORKORDER_DETAILS, WorkorderDetailsResponse.class)
            .headers(headers)
            .inlineVariables(workorderId);

        return HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets image by image id
     *
     * @param imageId - id to send
     * @return Object - response
     */
    public Object getImageById(String imageId) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.GET_IMAGES, null)
            .headers(headers)
            .inlineVariables(imageId);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Checks the process group is valid before proceeding.  This check has to be done to ensure the system doesn't
     * crash as per https://jira.apriori.com/browse/BA-1202
     *
     * @param processGroup - the process group
     */
    public void checkValidProcessGroup(String processGroup) {
        boolean match = false;

        for (String processGroupEnum : ProcessGroupEnum.getNames()) {
            if (processGroupEnum.equals(processGroup)) {
                match = true;
                break;
            }
        }
        if (!match) {
            throw new RuntimeException(String.format("Process Group '%s' is not valid", processGroup));
        }
    }

    /**
     * Loads CAD Metadata for an Assembly Component and adds to global Array List
     *
     * @param fileResponse - FileResponse - to use to load cad metadata
     */
    public void loadAssemblyComponentCadMetadataAddToArrayList(FileResponse fileResponse) {
        componentMetadataOutputs.add(loadCadMetadataSuppressError(fileResponse));
    }

    /**
     * Get Component Metadata Outputs ArrayList
     *
     * @return ArrayList of LoadCadMetadataOutputs
     */
    public ArrayList<LoadCadMetadataOutputs> getComponentMetadataOutputs() {
        return componentMetadataOutputs;
    }

    /**
     * Sets Current FileUploadOutput as Global Variable
     *
     * @param fileUploadOutputs - FileUploadOutputs to set
     */
    public void setCurrentFileUploadOutputs(FileUploadOutputs fileUploadOutputs) {
        this.currentFileUploadOutputs = fileUploadOutputs;
    }

    /**
     * Get Current FileUploadOutputs
     *
     * @return FileUploadOutputs instance currently set
     */
    public FileUploadOutputs getCurrentFileUploadOutputs() {
        return currentFileUploadOutputs;
    }

    /**
     * Sets Publish Scenario Iteration Key
     *
     * @param scenarioKey - scenario key
     * @return PublishScenarioIterationKey
     */
    private PublishScenarioIterationKey setPublishScenarioIterationKey(ScenarioKey scenarioKey) {
        return PublishScenarioIterationKey.builder()
            .iteration(getLatestIteration(scenarioKey))
            .scenarioKey(PublishScenarioKey.builder()
                .masterName(scenarioKey.getMasterName())
                .stateName(scenarioKey.getStateName())
                .typeName(scenarioKey.getTypeName())
                .workspaceId(scenarioKey.getWorkspaceId())
                .build())
            .build();
    }

    /**
     * Sets Cost Order Scenario Iteration
     *
     * @param scenarioKey - scenario key
     * @return CostOrderScenarioIteration
     */
    private CostOrderScenarioIteration setCostOrderScenarioIteration(ScenarioKey scenarioKey) {
        return CostOrderScenarioIteration.builder()
            .iteration(getLatestIteration(scenarioKey))
            .scenarioKey(CostOrderScenario.builder()
                .masterName(scenarioKey.getMasterName())
                .stateName(scenarioKey.getStateName())
                .typeName(scenarioKey.getTypeName())
                .workspaceId(scenarioKey.getWorkspaceId())
                .build())
            .build();
    }

    /**
     * Checks get workorder details
     *
     * @param workorderId - String
     * @return Object
     */
    private Object checkGetWorkorderDetails(String workorderId) {
        String status = checkWorkorderStatus(workorderId);
        if (status.equals("SUCCESS")) {
            WorkorderDetailsResponse workorderDetails = (WorkorderDetailsResponse) getWorkorderDetails(workorderId);
            return workorderDetails.getCommand().getOutputs();
        }
        return null;
    }

    /**
     * Initialize cost scenario
     *
     * @param scenarioKey  scenario iteration
     * @param processGroup process group
     * @return Integer
     */
    private Integer initializeCostScenario(Object fileObject, ScenarioKey scenarioKey, String processGroup) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.INITIALIZE_COST_SCENARIO, CreateWorkorderResponse.class)
            .headers(headers)
            .body(productionInfo(fileObject, scenarioKey, processGroup))
            .inlineVariables(
                scenarioKey.getWorkspaceId().toString(),
                scenarioKey.getTypeName(),
                UrlEscapers.urlFragmentEscaper().escape(scenarioKey.getMasterName()),
                scenarioKey.getStateName(),
                String.valueOf(getLatestIteration(scenarioKey)));

        return Integer.parseInt(jsonNode(HTTPRequest.build(requestEntity).post().getBody(), "id"));
    }

    /**
     * Gets costing iteration
     *
     * @param scenarioKey - the scenario key
     * @return int
     */
    private int getLatestIteration(ScenarioKey scenarioKey) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.GET_LATEST_ITERATION, CostIteration.class)
            .headers(headers)
            .inlineVariables(
                scenarioKey.getWorkspaceId().toString(),
                scenarioKey.getTypeName(),
                UrlEscapers.urlFragmentEscaper().escape(scenarioKey.getMasterName()),
                scenarioKey.getStateName()
            );

        return Integer.parseInt(jsonNode(HTTPRequest.build(requestEntity).get().getBody(), "iteration"));
    }

    /**
     * Checks the order status is successful
     *
     * @param workorderId - workorder id to send
     * @return String
     */
    private String checkWorkorderStatus(String workorderId) {
        long initialTime = System.currentTimeMillis() / 1000;
        String status;

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        do {
            setupHeaders("application/json");

            final RequestEntity requestEntity = RequestEntityUtil
                .init(CidWorkorderApiEnum.CHECK_WORKORDER_STATUS, null)
                .headers(headers)
                .inlineVariables(workorderId);

            status = HTTPRequest.build(requestEntity).get().getBody().replace("\"", "");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        } while (!status.equals(WorkorderStatusEnum.SUCCESS.getWorkorderStatus())
            && !status.equals(WorkorderStatusEnum.FAILED.getWorkorderStatus())
            && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return status;
    }

    /**
     * Submits the workorder for processing
     *
     * @param orderId - the order id
     */
    private void submitWorkorder(String orderId) {
        setupHeaders("application/json");

        final RequestEntity requestEntity = RequestEntityUtil
            .init(CidWorkorderApiEnum.SUBMIT_WORKORDER, SubmitWorkOrder.class)
            .headers(headers)
            .body(new FileWorkorder().setOrderIds(Collections.singletonList(orderId))
                .setAction("SUBMIT"));

        HTTPRequest.build(requestEntity).post();
    }

    /**
     * Sets the production info
     *
     * @param fileObject   - the file object
     * @param scenarioKey  - the scenario key
     * @param processGroup - the process group
     * @return ProductionInfo
     */
    private ProductionInfo productionInfo(Object fileObject, ScenarioKey scenarioKey, String processGroup) {
        NewPartRequest newPartRequest = (NewPartRequest) fileObject;
        int workspaceId = scenarioKey.getWorkspaceId();
        String typeName = scenarioKey.getTypeName();
        String stateName = scenarioKey.getStateName();
        String masterName = scenarioKey.getMasterName();

        return new ProductionInfo()
            .setScenarioKey(new ProductionInfoScenario()
                .setWorkspaceId(workspaceId)
                .setTypeName(typeName)
                .setStateName(stateName)
                .setMasterName(masterName))
            .setCompType(newPartRequest.getCompType())
            .setInitialized(false)
            .setAvailablePgNames(Arrays.asList(newPartRequest.getAvailablePg()))

            .setProcessGroupName(processGroup)
            .setPgEnabled(true)

            .setVpeBean(new ProductionInfoVpe()
                .setScenarioKey(new ProductionInfoScenarioKey()
                    .setWorkspaceId(workspaceId)
                    .setTypeName(typeName)
                    .setStateName(stateName)
                    .setMasterName(masterName))

                .setPrimaryPgName(processGroup)
                .setPrimaryVpeName(newPartRequest.getVpeName())
                .setAutoSelectedSecondaryVpes(null)
                .setUsePrimaryAsDefault(true)
                .setInitialized(false)

                .setMaterialCatalogKeyData(new MaterialCatalogKeyData().setFirst(newPartRequest.getVpeName())
                    .setSecond(newPartRequest.getVpeName())))

            .setSupportsMaterials(true)
            .setMaterialBean(new ProductionInfoMaterial().setInitialized(false)
                .setMaterialMode(newPartRequest.getMaterialMode())
                .setIsUserMaterialNameValid(false)
                .setIsCadMaterialNameValid(false))

            .setAnnualVolume(4400)
            .setAnnualVolumeOverridden(true)
            .setProductionLife(4)
            .setProductionLifeOverridden(false)
            .setBatchSizeOverridden(null)
            .setComputedBatchSize(458)
            .setBatchSizeOverridden(false)
            .setComponentsPerProduct(1)
            .setManuallyCosted(false)
            .setAvailableCurrencyCodes(Arrays.asList(newPartRequest.getCurrencyCode()))
            .setManualCurrencyCode(newPartRequest.getCurrencyCode())
            .setMachiningMode(newPartRequest.getMachiningMode())
            .setHasTargetCost(false)
            .setHasTargetFinishMass(null)
            .setHasTargetFinishMass(false)
            .setCadModelLoaded(true)
            .setThicknessVisible(true);
    }

    /**
     * Checks the json tree
     *
     * @param jsonProperties - the json properties
     * @param path           - the path
     * @return String
     */
    private String jsonNode(String jsonProperties, String path) {
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(jsonProperties);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new NullPointerException("Not able to read JsonNode");
        }
        return node.findPath(path).asText();
    }

    /**
     * Sets up header with content type and token
     *
     * @param contentType String
     */
    private void setupHeaders(String contentType) {
        String defaultString = "default";
        headers.put("Content-Type", contentType);
        headers.put("Accept", "*/*");
        headers.put("apriori.tenantgroup", defaultString);
        headers.put("apriori.tenant", defaultString);
        Object[] tokenArray = token.keySet().toArray();
        for (Object key : tokenArray) {
            headers.put(key.toString(), token.get(key));
        }
    }
}