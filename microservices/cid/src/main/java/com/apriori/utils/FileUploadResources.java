package com.apriori.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;
import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderInputs;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderScenario;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderScenarioIteration;
import com.apriori.entity.request.cost.productioninfo.ProductionInfo;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoMaterial;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoScenario;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoScenarioKey;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoVpe;
import com.apriori.entity.request.publish.createpublishworkorder.PublishInputs;
import com.apriori.entity.request.publish.createpublishworkorder.PublishScenarioIterationKey;
import com.apriori.entity.request.publish.createpublishworkorder.PublishScenarioKey;
import com.apriori.entity.response.CreateWorkorderResponse;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.cost.iterations.CostIteration;
import com.apriori.entity.response.publish.publishworkorderresult.PublishResultOutputs;
import com.apriori.entity.response.upload.FileUploadInputs;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.FileWorkOrder;
import com.apriori.entity.response.upload.GenerateAssemblyImagesInputs;
import com.apriori.entity.response.upload.GenerateAssemblyImagesOutputs;
import com.apriori.entity.response.upload.GeneratePartImagesInputs;
import com.apriori.entity.response.upload.GeneratePartImagesOutputs;
import com.apriori.entity.response.upload.LoadCadMetadataInputs;
import com.apriori.entity.response.upload.LoadCadMetadataOutputs;
import com.apriori.entity.response.upload.ScenarioKey;
import com.apriori.entity.response.upload.WorkorderCommand;
import com.apriori.entity.response.upload.WorkorderCommands;
import com.apriori.entity.response.upload.WorkorderDetailsResponse;
import com.apriori.entity.response.upload.WorkorderRequest;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;

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
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);
    private static final long WAIT_TIME = 180;

    private static int iteration;
    private static final HashMap<String, String> token = new APIAuthentication()
            .initAuthorizationHeaderNoContent("aPrioriCIGenerateUser@apriori.com");

    private final String orderSuccess = "SUCCESS";
    private final String orderFailed = "FAILED";
    private final String acceptHeader = "Accept";
    private final String contentType = "Content-Type";
    private final String applicationJson = "application/json";
    private final String textPlain = "text/plain";
    Map<String, String> headers = new HashMap<>();
    private String baseUrl = System.getProperty("baseUrl");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     *  Uploads part to CID
     *
     * @param fileName - name of file to upload
     * @param processGroup - process group of file
     * @return FileResponse - response to use in next call
     */
    public FileResponse initialisePartUpload(String fileName, String processGroup) {
        return initializeFileUpload(fileName, processGroup);
    }

    /**
     * Upload part
     *
     * @param fileResponse response from file upload initialise
     * @param scenarioName scenario name to use
     * @return FileUploadOutputs
     */
    public FileUploadOutputs uploadPart(FileResponse fileResponse, String scenarioName) {
        String fileUploadWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_FILE.getWorkorderCommand(),
                new FileUploadInputs()
                        .setScenarioName(scenarioName)
                        .setFileKey(fileResponse.getResponse().getIdentity())
                        .setFileName(fileResponse.getResponse().getFilename()));
        submitWorkorder(fileUploadWorkorderId);
        return objectMapper.convertValue(
                checkGetWorkorderDetails(fileUploadWorkorderId),
                FileUploadOutputs.class
        );
    }

    /**
     * Loads CAD Metadata
     *
     * @param fileResponse - response from file upload
     * @return LoadCadMetadataOutputs - outputs to use in next call
     */
    public LoadCadMetadataOutputs loadCadMetadata(FileResponse fileResponse) {
        String loadCadMetadataWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_METADATA.getWorkorderCommand(),
                new LoadCadMetadataInputs()
                        .setFileMetadataIdentity(fileResponse.getResponse().getIdentity())
                        .setRequestedBy(fileResponse.getResponse().getUserIdentity())
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
     * @param fileResponse - response from file upload
     * @param loadCadMetadataOutputs - output from load cad metadata
     * @return GeneratePartImagesOutputs - response to use in next call
     */
    public GeneratePartImagesOutputs generatePartImages(FileResponse fileResponse,
                                                        LoadCadMetadataOutputs loadCadMetadataOutputs) {
        String generatePartImagesWorkorderId = createWorkorder(
                WorkorderCommands.GENERATE_PART_IMAGES.getWorkorderCommand(),
                new GeneratePartImagesInputs()
                        .setCadMetadataIdentity(loadCadMetadataOutputs.getCadMetadataIdentity())
                        .setRequestedBy(fileResponse.getResponse().getUserIdentity())
        );
        submitWorkorder(generatePartImagesWorkorderId);

        return objectMapper.convertValue(
                checkGetWorkorderDetails(generatePartImagesWorkorderId),
                GeneratePartImagesOutputs.class
        );
    }

    /**
     * Generates assembly images
     *
     * @param fileResponse - response from file upload
     * @param loadCadMetadataOutputs - assembly load cad metadata response
     * @param loadCadMetadataOutputs2 - component 1 load cad metadata response
     * @param loadCadMetadataOutputs3 - component 2 load cad metadata response
     * @return GenerateAssemblyImagesOutputs
     */
    public GenerateAssemblyImagesOutputs generateAssemblyImages(FileResponse fileResponse,
                                                            LoadCadMetadataOutputs loadCadMetadataOutputs,
                                                            LoadCadMetadataOutputs loadCadMetadataOutputs2,
                                                            LoadCadMetadataOutputs loadCadMetadataOutputs3) {
        GenerateStringUtil generateStringUtil = new GenerateStringUtil();
        List<GenerateAssemblyImagesInputs> subComponentsList = new ArrayList<>();
        subComponentsList.add(new GenerateAssemblyImagesInputs()
                .setComponentIdentity(generateStringUtil.getRandomString())
                .setScenarioIdentity(generateStringUtil.getRandomString())
                .setCadMetadataIdentity(loadCadMetadataOutputs2.getCadMetadataIdentity())
        );
        subComponentsList.add(new GenerateAssemblyImagesInputs()
                .setComponentIdentity(generateStringUtil.getRandomString())
                .setScenarioIdentity(generateStringUtil.getRandomString())
                .setCadMetadataIdentity(loadCadMetadataOutputs3.getCadMetadataIdentity())
        );
        String generateAssemblyImagesWorkorderId = createWorkorder(
                WorkorderCommands.GENERATE_ASSEMBLY_IMAGES.getWorkorderCommand(),
                new GenerateAssemblyImagesInputs()
                        .setComponentIdentity(generateStringUtil.getRandomString())
                        .setScenarioIdentity(generateStringUtil.getRandomString())
                        .setCadMetadataIdentity(loadCadMetadataOutputs.getCadMetadataIdentity())
                        .setSubComponents(subComponentsList)
                        .setRequestedBy(fileResponse.getResponse().getUserIdentity())
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
     * @param fileUploadOutputs - output from file upload
     * @param processGroup - process group
     * @return CostOrderStatusOutputs
     */
    public CostOrderStatusOutputs costPart(Object productionInfoInputs, FileUploadOutputs fileUploadOutputs, String processGroup) {
        int inputSetId = initializeCostScenario(
                productionInfoInputs,
                fileUploadOutputs.getScenarioIterationKey().getScenarioKey(),
                processGroup
        );

        String costWorkorderId = createWorkorder(WorkorderCommands.COSTING.getWorkorderCommand(),
                new CostOrderInputs()
                        .setInputSetId(inputSetId)
                        .setScenarioIterationKey(new CostOrderScenarioIteration()
                                .setIteration(iteration)
                                .setScenarioKey(new CostOrderScenario()
                                        .setMasterName(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getMasterName())
                                        .setStateName(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getStateName())
                                        .setTypeName(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getTypeName())
                                        .setWorkspaceId(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId())
                                ))
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
                new PublishInputs()
                        .setScenarioIterationKey(new PublishScenarioIterationKey()
                                .setIteration(iteration)
                                .setScenarioKey(new PublishScenarioKey()
                                        .setMasterName(costOutputs.getScenarioIterationKey().getScenarioKey().getMasterName())
                                        .setStateName(costOutputs.getScenarioIterationKey().getScenarioKey().getStateName())
                                        .setTypeName(costOutputs.getScenarioIterationKey().getScenarioKey().getTypeName())
                                        .setWorkspaceId(costOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId())
                                ))
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
        assertThat(imageResponse, is(notNullValue()));

        assertThat(Base64.isBase64(imageResponse), is(equalTo(true)));
    }

    /**
     * Initializes file upload
     *
     * @param fileName - the filename
     * @param processGroup - the process group
     * @return FileResponse
     */
    private FileResponse initializeFileUpload(String fileName, String processGroup) {
        String url = baseUrl.concat("apriori/cost/session/ws/files");

        headers.put(contentType, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup),fileName)))
            .setFormParams(new FormParams().use("filename", fileName));

        return (FileResponse) GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getResponseEntity();
    }

    /**
     * Creates workorder
     *
     * @param commandType String
     * @param inputs Object
     * @return String
     */
    private String createWorkorder(String commandType, Object inputs) {
        String fileURL = baseUrl.concat("apriori/cost/session/ws/workorder/orders");

        headers.put(contentType, applicationJson);

        RequestEntity workorderRequestEntity = RequestEntity.init(fileURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new WorkorderRequest()
                .setCommand(new WorkorderCommand(
                    commandType,
                    inputs))
                );

        String workorderId;
        try {
            workorderId = jsonNode(GenericRequestUtil.post(workorderRequestEntity, new RequestAreaApi()).getBody(), "id");
        } catch (Exception e) {
            workorderId = jsonNode(GenericRequestUtil.post(workorderRequestEntity, new RequestAreaApi()).getBody(), "id");
            e.printStackTrace();
        }
        return workorderId;
    }

    /**
     * Checks if workorder processing has successfully completed
     *
     * @param workorderId - workorder id
     * @return Object
     */
    private Object getWorkorderDetails(String workorderId) {
        String workorderDetailsURL = baseUrl.concat(
                String.format("apriori/cost/session/ws/workorder/orders/%s", workorderId)
        );

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(workorderDetailsURL, WorkorderDetailsResponse.class)
            .setHeaders(headers)
            .setHeaders(token);

        return GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getResponseEntity();
    }

    /**
     * Gets image by image id
     *
     * @param imageId - id to send
     * @return Object - response
     */
    public Object getImageById(String imageId) {
        String getImagesUrl = baseUrl.concat("ws/viz/images/").concat(imageId);

        headers.put(acceptHeader, textPlain);

        RequestEntity orderRequestEntity = RequestEntity.init(getImagesUrl, null)
                .setHeaders(headers)
                .setHeaders(token);

        return GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody();
    }

    /**
     * Get images by scenario iteration key
     *
     * @param scenarioKey - scenario iteration
     * @param imageType - web or desktop image
     * @return Object - response
     */
    public Object getImageByScenarioIterationKey(ScenarioKey scenarioKey, String imageType) {
        iteration = getLatestIteration(
                token,
                scenarioKey.getTypeName(),
                scenarioKey.getMasterName(),
                scenarioKey.getStateName(),
                scenarioKey.getWorkspaceId()
        );
        String getImageUrl = baseUrl.concat(
                String.format("ws/viz/%s/scenarios/%s/%s/%s/iterations/%s/images/%s",
                        scenarioKey.getWorkspaceId(),
                        scenarioKey.getTypeName(),
                        scenarioKey.getMasterName(),
                        scenarioKey.getStateName(),
                        iteration,
                        imageType));

        RequestEntity requestEntity = RequestEntity.init(getImageUrl, null)
                .setHeaders(token);

        return GenericRequestUtil.get(requestEntity, new RequestAreaApi()).getBody();
    }

    /**
     * Checks the process group is valid before proceeding.  This check has to be done to ensure the system doesn't crash as per https://jira.apriori.com/browse/BA-1202
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
     * @param scenarioKey scenario iteration
     * @param processGroup process group
     * @return Integer
     */
    private Integer initializeCostScenario(Object fileObject, ScenarioKey scenarioKey, String processGroup) {
        iteration = getLatestIteration(
                token,
                scenarioKey.getTypeName(),
                scenarioKey.getMasterName(),
                scenarioKey.getStateName(),
                scenarioKey.getWorkspaceId()
        );
        String orderURL = baseUrl.concat(
                String.format("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s/iterations/%s/production-info",
                scenarioKey.getWorkspaceId(),
                scenarioKey.getTypeName(),
                UrlEscapers.urlFragmentEscaper().escape(scenarioKey.getMasterName()),
                scenarioKey.getStateName(),
                iteration));

        headers.put(contentType, applicationJson);

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(
                productionInfo(fileObject, scenarioKey, processGroup));

        return Integer.parseInt(jsonNode(GenericRequestUtil.post(costRequestEntity, new RequestAreaApi()).getBody(), "id"));
    }

    /**
     * Gets costing iteration
     *
     * @param token - the user token
     * @param typeName - the type name
     * @param masterName - the master name
     * @param stateName - the state name
     * @param workspaceId - the workspace id
     * @return int
     */
    private int getLatestIteration(HashMap<String, String> token, String typeName, String masterName, String stateName, Integer workspaceId) {
        String orderURL = baseUrl.concat(
                String.format("apriori/cost/session/ws/workspace/%s/scenarios/%s/%s/%s",
                        workspaceId,
                        typeName,
                        UrlEscapers.urlFragmentEscaper().escape(masterName),
                        stateName)
        );

        RequestEntity iterationRequestEntity = RequestEntity.init(orderURL, CostIteration.class)
            .setHeaders(headers)
            .setHeaders(token);

        return Integer.parseInt(jsonNode(GenericRequestUtil.get(iterationRequestEntity, new RequestAreaApi()).getBody(), "iteration"));
    }

    /**
     * Checks the order status is successful
     *
     * @param workorderId - workorder id to send
     * @return String
     */
    private String checkWorkorderStatus(String workorderId) {
        long initialTime = System.currentTimeMillis() / 1000;
        RequestEntity requestEntityBody;
        String status;

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        do {
            String orderURL = baseUrl.concat(
                    String.format("apriori/cost/session/ws/workorder/orderstatus/%s", workorderId));

            requestEntityBody = RequestEntity.init(orderURL, null)
                    .setHeaders(headers)
                    .setHeaders(token);

            status = GenericRequestUtil.get(requestEntityBody, new RequestAreaApi()).getBody().replace("\"", "");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        } while (!status.equals(orderSuccess) && !status.equals(orderFailed) && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return status;
    }

    /**
     * Submits the workorder for processing
     *
     * @param orderId - the order id
     */
    private void submitWorkorder(String orderId) {
        String orderURL = baseUrl.concat("apriori/cost/session/ws/workorder/orderstatus");

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileWorkOrder().setOrderIds(Collections.singletonList(orderId))
                .setAction("SUBMIT"));

        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
    }

    /**
     * Sets the production info
     *
     * @param fileObject - the file object
     * @param scenarioKey - the scenario key
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
}