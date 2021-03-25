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
import com.apriori.entity.request.cost.createcostworkorder.CostOrderCommand;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderCommandType;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderInputs;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderScenario;
import com.apriori.entity.request.cost.createcostworkorder.CostOrderScenarioIteration;
import com.apriori.entity.request.cost.productioninfo.ProductionInfo;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoMaterial;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoScenario;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoScenarioKey;
import com.apriori.entity.request.cost.productioninfo.ProductionInfoVpe;
import com.apriori.entity.request.publish.createpublishworkorder.PublishCommand;
import com.apriori.entity.request.publish.createpublishworkorder.PublishInputs;
import com.apriori.entity.request.publish.createpublishworkorder.PublishScenarioIterationKey;
import com.apriori.entity.request.publish.createpublishworkorder.PublishScenarioKey;
import com.apriori.entity.request.publish.createpublishworkorder.PublishWorkOrderInfo;
import com.apriori.entity.response.CreateWorkorderResponse;
import com.apriori.entity.response.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.entity.response.cost.costworkorderstatus.ListOfCostOrderStatuses;
import com.apriori.entity.response.cost.iterations.ListOfCostIterations;
import com.apriori.entity.response.upload.FileCommand;
import com.apriori.entity.response.upload.FileOrdersUpload;
import com.apriori.entity.response.upload.FileUploadInputs;
import com.apriori.entity.response.upload.FileUploadOrder;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.GeneratePartImagesInputs;
import com.apriori.entity.response.upload.LoadCadMetadataInputs;
import com.apriori.entity.response.upload.GeneratePartImagesOutputs;
import com.apriori.entity.response.upload.LoadCadMetadataOutputs;
import com.apriori.entity.response.upload.WorkorderCommands;
import com.apriori.entity.response.upload.WorkorderDetailsResponse;
import com.apriori.entity.response.upload.FileWorkOrder;
import com.apriori.entity.response.upload.WorkorderRequest;
import com.apriori.entity.response.upload.WorkorderCommand;
import com.apriori.entity.response.upload.WorkorderStatusResponse;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.net.UrlEscapers;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);
    private static final long WAIT_TIME = 180;

    private static String identity;
    private static String fileIdentity;
    private static String userIdentity;
    private static String orderId;
    private static int inputSetId;
    private static String stateName;
    private static int workspaceId;
    private static String typeName;
    private static String masterName;
    private static String costWorkOrderId;
    private static String publishWorkOrderId;
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
    NewPartRequest newPartRequest = null;
    private String baseUrl = System.getProperty("baseUrl");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Method to upload part, load CAD metadata and generate part images
     *
     * @param fileName - file to upload
     * @param scenarioName - scenario name to use
     * @param processGroup - process group to use
     */
    public void uploadLoadCadMetadataGeneratePartImages(String fileName, String scenarioName, String processGroup) {
        // Create, submit and check file upload workorder
        FileResponse fileResponse = initializeFileUpload(fileName, processGroup);
        String fileUploadWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_FILE.getWorkorderCommand(),
                new FileUploadInputs()
                        .setScenarioName(scenarioName)
                        .setFileKey(fileResponse.getResponse().getIdentity())
                        .setFileName(fileName));
        submitWorkorder(fileUploadWorkorderId);
        FileUploadOutputs fileUploadOutputs = objectMapper.convertValue(
                checkGetWorkorderDetails(fileUploadWorkorderId),
                FileUploadOutputs.class
        );

        // Create, submit and check Load CAD Metadata workorder
        String loadCadMetadataWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_METADATA.getWorkorderCommand(),
                new LoadCadMetadataInputs()
                        .setFileMetadataIdentity(fileResponse.getResponse().getIdentity())
                        .setRequestedBy(fileResponse.getResponse().getUserIdentity())
        );
        submitWorkorder(loadCadMetadataWorkorderId);
        LoadCadMetadataOutputs loadCadMetadataOutputs = objectMapper.convertValue(
                checkGetWorkorderDetails(loadCadMetadataWorkorderId),
                LoadCadMetadataOutputs.class
        );

        // Create, submit and check Generate Part Images workorder
        String generatePartImagesWorkorderId = createWorkorder(
                WorkorderCommands.GENERATE_PART_IMAGES.getWorkorderCommand(),
                new GeneratePartImagesInputs()
                    .setCadMetadataIdentity(loadCadMetadataOutputs.getCadMetadataIdentity())
                    .setRequestedBy(fileResponse.getResponse().getUserIdentity())
        );
        submitWorkorder(generatePartImagesWorkorderId);
        GeneratePartImagesOutputs generatePartImagesOutputs = objectMapper.convertValue(
                checkGetWorkorderDetails(generatePartImagesWorkorderId),
                GeneratePartImagesOutputs.class
        );

        // Get images
        String webImageResponse = getAllImages(generatePartImagesOutputs.getWebImageIdentity()).toString();
        String desktopImageResponse = getAllImages(generatePartImagesOutputs.getDesktopImageIdentity()).toString();
        String thumbnailImageResponse = getAllImages(generatePartImagesOutputs.getThumbnailImageIdentity()).toString();

        assertThat(webImageResponse, is(notNullValue()));
        assertThat(desktopImageResponse, is(notNullValue()));
        assertThat(thumbnailImageResponse, is(notNullValue()));

        assertThat(Base64.isBase64(webImageResponse), is(equalTo(true)));
        assertThat(Base64.isBase64(desktopImageResponse), is(equalTo(true)));
        assertThat(Base64.isBase64(thumbnailImageResponse), is(equalTo(true)));
    }

    /**
     * Method to upload, cost and publish a scenario
     *
     * @param fileObject   - the file object
     * @param fileName     - the file name
     * @param scenarioName - the scenario name
     * @param processGroup - the process group
     */
    public void uploadCostPublishApi(Object fileObject, String fileName, String scenarioName, String processGroup) {
        checkValidProcessGroup(processGroup);

        FileResponse fileResponse = initializeFileUpload(fileName, processGroup);
        String fileUploadWorkorderId = createWorkorder(WorkorderCommands.LOAD_CAD_FILE.getWorkorderCommand(),
                new FileUploadInputs()
                        .setScenarioName(scenarioName)
                        .setFileKey(fileResponse.getResponse().getIdentity())
                        .setFileName(fileName));
        submitWorkorder(fileUploadWorkorderId);
        FileUploadOutputs fileUploadOutputs = objectMapper.convertValue(
                checkGetWorkorderDetails(fileUploadWorkorderId),
                FileUploadOutputs.class
        );

        initializeCostScenario(
                fileUploadOutputs,
                fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getTypeName(),
                fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getMasterName(),
                fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getStateName(),
                processGroup,
                fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId());
        String costWorkorderId = createWorkorder(WorkorderCommands.COSTING.getWorkorderCommand(),
                new CostOrderInputs()
                        .setInputSetId(inputSetId)
                .setScenarioIterationKey(new CostOrderScenarioIteration().setIteration(iteration)
                        .setScenarioKey(new CostOrderScenario()
                                .setMasterName(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getMasterName())
                                .setStateName(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getStateName())
                                .setTypeName(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getTypeName())
                                .setWorkspaceId(fileUploadOutputs.getScenarioIterationKey().getScenarioKey().getWorkspaceId()))));
        submitWorkorder(costWorkorderId);
        CostOrderStatusOutputs costOutputs = objectMapper.convertValue(
                checkGetWorkorderDetails(fileUploadWorkorderId),
                CostOrderStatusOutputs.class
        );
        /*
        String createPublishWorkorderId = createPublishScenario();
        submitPublishWorkOrder(createPublishWorkorderId);
        checkPublishResult(createPublishWorkorderId);*/
    }

    /**
     * Initializes file upload
     *
     * @param fileName - the filename
     */
    private FileResponse initializeFileUpload(String fileName, String processGroup) {
        String url = baseUrl + "apriori/cost/session/ws/files";

        headers.put(contentType, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup),fileName)))
            .setFormParams(new FormParams().use("filename", fileName));

        return (FileResponse) GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getResponseEntity();
    }

    /**
     * Creates file upload
     *
     * @param fileName     - the file name
     * @param scenarioName - the scenario name
     */
    private String createFileUploadWorkorder(String fileName, String scenarioName) {
        String fileURL = baseUrl + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileCommand()
                .setCommand(new FileOrdersUpload()
                    .setCommandType("LOADCADFILE")
                    .setInputs(new FileUploadOrder()
                        .setScenarioName(scenarioName)
                        .setFileKey(fileIdentity)
                        .setFileName(fileName.replaceAll("\\s", "")))));

        return jsonNode(GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Creates file upload
     */
    private String createWorkorder(String commandType, Object inputs) {
        String fileURL = baseUrl + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity workorderRequestEntity = RequestEntity.init(fileURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new WorkorderRequest()
                .setCommand(new WorkorderCommand(
                    commandType,
                    inputs))
                );

        return jsonNode(GenericRequestUtil.post(workorderRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Checks if workorder processing has successfully completed
     *
     * @param workorderId - workorder id
     * @return Object
     */
    private Object getWorkorderDetails(String workorderId) {
        String workorderDetailsURL = baseUrl + "apriori/cost/session/ws/workorder/orders/" + workorderId;

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(workorderDetailsURL, WorkorderDetailsResponse.class)
            .setHeaders(headers)
            .setHeaders(token);

        return GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getResponseEntity();
    }

    /**
     * Gets all images by image id
     *
     * @param imageId - id to send
     * @return Object - response
     */
    private Object getAllImages(String imageId) {
        String getImagesUrl = baseUrl.concat("ws/viz/images/").concat(imageId);

        headers.put(acceptHeader, textPlain);

        RequestEntity orderRequestEntity = RequestEntity.init(getImagesUrl, null)
                .setHeaders(headers)
                .setHeaders(token);

        return GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody();
    }

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
     * @param fileObject - file object
     * @param typeName - type name
     * @param masterName - master name
     * @param stateName - state name
     * @param processGroup process group
     */
    private void initializeCostScenario(Object fileObject, String typeName, String masterName, String stateName, String processGroup, Integer workspaceId) {
        iteration = getLatestIteration(token, typeName, masterName, stateName, workspaceId);
        String orderURL = baseUrl + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + UrlEscapers.urlFragmentEscaper().escape(masterName) + "/" + stateName + "/iterations/" + iteration + "/production-info";

        headers.put(contentType, applicationJson);

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(
                productionInfo(fileObject, workspaceId, typeName, stateName, masterName, processGroup));

        inputSetId = Integer.parseInt(jsonNode(GenericRequestUtil.post(costRequestEntity, new RequestAreaApi()).getBody(), "id"));
    }

    /**
     * Create cost work order
     */
    private String createCostWorkOrder() {
        iteration = getLatestIteration(token, "", "", "", 2);
        String orderURL = baseUrl + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new CostOrderCommand().setCommand(new CostOrderCommandType()
                .setCommandType("COSTING")
                .setInputs(new CostOrderInputs().setInputSetId(inputSetId)
                    .setScenarioIterationKey(new CostOrderScenarioIteration().setIteration(iteration)
                        .setScenarioKey(new CostOrderScenario().setMasterName(masterName)
                            .setStateName(stateName)
                            .setTypeName(typeName)
                            .setWorkspaceId(workspaceId))))));

        return jsonNode(GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Submits scenario for costing
     */
    private void submitCostWorkOrder() {
        submitWorkorder(costWorkOrderId);
    }

    /**
     * Gets costing iteration
     *
     * @param token - the user token
     * @return
     */
    private int getLatestIteration(HashMap<String, String> token, String typeName, String masterName, String stateName, Integer workspaceId) {
        String orderURL = baseUrl + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + UrlEscapers.urlFragmentEscaper().escape(masterName) + "/" + stateName + "/iterations";

        RequestEntity iterationRequestEntity = RequestEntity.init(orderURL, ListOfCostIterations.class)
            .setHeaders(headers)
            .setHeaders(token);

        return Integer.parseInt(jsonNode(GenericRequestUtil.get(iterationRequestEntity, new RequestAreaApi()).getBody(), "iteration"));
    }

    /**
     * Initializes publish scenario
     */
    private String createPublishScenario() {
        iteration = getLatestIteration(token, "", "", "", 2);
        String orderURL = baseUrl + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity publishRequestEntity = RequestEntity.init(orderURL, CreateWorkorderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new PublishWorkOrderInfo().setCommand(
                new PublishCommand().setCommandType("PUBLISH")
                    .setInputs(new PublishInputs().setOverwrite(false)
                        .setLock(false)
                        .setScenarioIterationKey(new PublishScenarioIterationKey().setScenarioKey(
                            new PublishScenarioKey().setTypeName(typeName)
                                .setStateName(stateName)
                                .setWorkspaceId(workspaceId)
                                .setMasterName(masterName))
                            .setIteration(iteration)))));

        return jsonNode(GenericRequestUtil.post(publishRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Submits publish work order
     */
    private void submitPublishWorkOrder(String workorderId) {
        submitWorkorder(publishWorkOrderId);
    }

    /**
     * Checks costing status
     *
     * @param token - the user token
     * @return request entity
     */
    private RequestEntity checkCostOrder(HashMap<String, String> token) {
        String orderURL = baseUrl + "apriori/cost/session/ws/workorder/orders/by-id?id=" + costWorkOrderId;

        return RequestEntity.init(orderURL, ListOfCostOrderStatuses.class)
            .setHeaders(headers)
            .setHeaders(token);
    }

    /**
     * Checks publish order
     *
     * @param token - the user token
     * @param klass - the class
     * @return request entity
     */
    private RequestEntity checkPublishOrder(HashMap<String, String> token, Class klass) {
        String orderURL = baseUrl + "apriori/cost/session/ws/workorder/orders/" + publishWorkOrderId;

        return RequestEntity.init(orderURL, klass)
            .setHeaders(headers)
            .setHeaders(token);
    }

    /**
     * Checks publish result
     */
    private void checkPublishResult(String workorderId) {
        checkWorkorderStatus(workorderId);
    }

    /**
     * Checks the order status is successful
     *
     * @param workorderId - workorder id to send
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
            String orderURL = baseUrl + "apriori/cost/session/ws/workorder/orderstatus/" + workorderId;

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
        String orderURL = baseUrl + "apriori/cost/session/ws/workorder/orderstatus";

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
     * @param workspaceId  - the workspace id
     * @param typeName     - type name
     * @param stateName    - the state name
     * @param masterName   - the master name
     * @param processGroup - the process group
     * @return production info
     */
    private ProductionInfo productionInfo(Object fileObject, int workspaceId, String typeName, String stateName, String masterName, String processGroup) {
        newPartRequest = (NewPartRequest) fileObject;

        return new ProductionInfo()
            .setScenarioKey(new ProductionInfoScenario().setWorkspaceId(workspaceId)
                .setTypeName(typeName)
                .setStateName(stateName)
                .setMasterName(masterName))
            .setCompType(newPartRequest.getCompType())
            .setInitialized(false)
            .setAvailablePgNames(Arrays.asList(newPartRequest.getAvailablePg()))

            .setProcessGroupName(processGroup)
            .setPgEnabled(true)

            .setVpeBean(new ProductionInfoVpe()
                .setScenarioKey(new ProductionInfoScenarioKey().setWorkspaceId(workspaceId)
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
     * Checks the process group is valid before proceeding.  This check has to be done to ensure the system doesn't crash as per https://jira.apriori.com/browse/BA-1202
     * @param processGroup - the process group
     */
    private void checkValidProcessGroup(String processGroup) {
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
}