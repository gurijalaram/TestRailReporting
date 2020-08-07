package com.apriori.utils;

import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.CostOrderCommand;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.CostOrderCommandType;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.CostOrderInputs;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.CostOrderScenario;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.CostOrderScenarioIteration;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfo;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfoMaterial;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfoScenario;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfoScenarioKey;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfoVpe;
import com.apriori.apibase.services.cid.objects.publish.createpublishworkorder.PublishCommand;
import com.apriori.apibase.services.cid.objects.publish.createpublishworkorder.PublishInputs;
import com.apriori.apibase.services.cid.objects.publish.createpublishworkorder.PublishScenarioIterationKey;
import com.apriori.apibase.services.cid.objects.publish.createpublishworkorder.PublishScenarioKey;
import com.apriori.apibase.services.cid.objects.publish.createpublishworkorder.PublishWorkOrderInfo;
import com.apriori.apibase.services.cid.objects.request.NewPartRequest;
import com.apriori.apibase.services.cid.objects.response.FileOrderResponse;
import com.apriori.apibase.services.cid.objects.response.cost.costworkorderstatus.ListOfCostOrderStatuses;
import com.apriori.apibase.services.cid.objects.response.cost.iterations.ListOfCostIterations;
import com.apriori.apibase.services.cid.objects.response.publish.publishworkorderresult.PublishWorkOrderInfoResult;
import com.apriori.apibase.services.cid.objects.response.upload.FileCommand;
import com.apriori.apibase.services.cid.objects.response.upload.FileOrdersUpload;
import com.apriori.apibase.services.cid.objects.response.upload.FileUploadOrder;
import com.apriori.apibase.services.cid.objects.response.upload.FileUploadWorkOrder;
import com.apriori.apibase.services.cid.objects.response.upload.FileWorkOrder;
import com.apriori.apibase.services.fms.objects.FileResponse;
import com.apriori.apibase.services.response.objects.MaterialCatalogKeyData;
import com.apriori.apibase.services.response.objects.SubmitWorkOrder;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.UrlEscapers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);
    private static final long WAIT_TIME = 120;

    private static String identity;
    private static String orderId;
    private static int inputSetId;
    private static String stateName;
    private static int workspaceId;
    private static String typeName;
    private static String masterName;
    private static int iteration;
    private static String costWorkOrderId;
    private static int costingIteration;
    private static String publishWorkOrderId;

    private final String ORDER_SUCCESS = "SUCCESS";
    private final String ORDER_FAILED = "FAILED";
    private final String CONTENT_TYPE = "Content-Type";
    private final String APPLICATION_JSON = "application/json";
    Map<String, String> headers = new HashMap<>();
    NewPartRequest newPartRequest = null;


    /**
     * Method to upload, cost and publish a scenario
     *
     * @param token        - the token
     * @param fileObject   - the file object
     * @param fileName     - the file name
     * @param scenarioName - the scenario name
     * @param processGroup - the process group
     */
    public void uploadCostPublishApi(HashMap<String, String> token, Object fileObject, String fileName, String scenarioName, String processGroup) {
        initializeFileUpload(token, fileName);
        createFileUploadWorkOrder(token, fileName, scenarioName);
        submitFileUploadWorkOrder(token);
        checkFileWorkOrderStatus(token);
        initializeCostScenario(token, fileObject, processGroup);
        createCostWorkOrder(token);
        submitCostWorkOrder(token);
        checkCostResult(token);
        getCostingIteration(token);
        initializePublishScenario(token);
        submitPublishWorkOrder(token);
        checkPublishResult(token);
    }

    /**
     * Initializes file upload
     *
     * @param token    - the user token
     * @param fileName - the filename
     */
    private void initializeFileUpload(HashMap<String, String> token, String fileName) {
        String url = Constants.getBaseUrl() + "apriori/cost/session/ws/files";

        headers.put(CONTENT_TYPE, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(fileName)))
            .setFormParams(new FormParams().use("filename", fileName));

        identity = jsonNode(GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getBody(), "identity");
    }

    /**
     * Creates file upload
     *
     * @param token        - the user token
     * @param fileName     - the file name
     * @param scenarioName - the scenario name
     */
    private void createFileUploadWorkOrder(HashMap<String, String> token, String fileName, String scenarioName) {
        String fileURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileCommand()
                .setCommand(new FileOrdersUpload()
                    .setCommandType("LOADCADFILE")
                    .setInputs(new FileUploadOrder().setScenarioName(scenarioName)
                        .setFileKey(identity)
                        .setFileName(fileName.replaceAll("\\s", "")))));

        orderId = jsonNode(GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Submits file for upload
     *
     * @param token - the user token
     */
    private void submitFileUploadWorkOrder(HashMap<String, String> token) {
        submitOrder(token, orderId);
    }

    /**
     * Checks file status
     *
     * @param token - the user token
     */
    private void checkFileWorkOrderStatus(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/" + orderId;

        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, FileUploadWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token);

        String orderBody = checkOrderSuccessful(orderRequestEntity);

        workspaceId = Integer.parseInt(jsonNode(orderBody, "workspaceId"));
        typeName = jsonNode(orderBody, "typeName");
        masterName = jsonNode(orderBody, "masterName");
        stateName = jsonNode(orderBody, "stateName");
        iteration = Integer.parseInt(jsonNode(orderBody, "iteration"));
    }

    /**
     * Initialize cost scenario
     *
     * @param token - the user token
     */
    private void initializeCostScenario(HashMap<String, String> token, Object fileObject, String processGroup) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + UrlEscapers.urlFragmentEscaper().escape(masterName) + "/" + stateName + "/iterations/" + iteration + "/production-info";

        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(
                productionInfo(fileObject, workspaceId, typeName, stateName, masterName, processGroup));

        inputSetId = Integer.parseInt(jsonNode(GenericRequestUtil.post(costRequestEntity, new RequestAreaApi()).getBody(), "id"));
    }

    /**
     * Create cost work order
     *
     * @param token - the user token
     */
    private void createCostWorkOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, FileOrderResponse.class)
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

        costWorkOrderId = jsonNode(GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Submits scenario for costing
     *
     * @param token - the user token
     */
    private void submitCostWorkOrder(HashMap<String, String> token) {
        submitOrder(token, costWorkOrderId);
    }

    /**
     * Checks cost result
     *
     * @param token - the user token
     */
    private void checkCostResult(HashMap<String, String> token) {
        checkOrderSuccessful(checkCostOrder(token));
    }

    /**
     * Gets costing iteration
     *
     * @param token - the user token
     */
    private void getCostingIteration(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + UrlEscapers.urlFragmentEscaper().escape(masterName) + "/" + stateName + "/iterations";

        RequestEntity iterationRequestEntity = RequestEntity.init(orderURL, ListOfCostIterations.class)
            .setHeaders(headers)
            .setHeaders(token);

        costingIteration = Integer.parseInt(jsonNode(GenericRequestUtil.get(iterationRequestEntity, new RequestAreaApi()).getBody(), "iteration"));
    }

    /**
     * Initializes publish scenario
     *
     * @param token - the user token
     */
    private void initializePublishScenario(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        headers.put(CONTENT_TYPE, APPLICATION_JSON);

        RequestEntity publishRequestEntity = RequestEntity.init(orderURL, FileOrderResponse.class)
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
                            .setIteration(costingIteration)))));

        publishWorkOrderId = jsonNode(GenericRequestUtil.post(publishRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    /**
     * Submits publish work order
     *
     * @param token - the user token
     */
    private void submitPublishWorkOrder(HashMap<String, String> token) {
        submitOrder(token, publishWorkOrderId);
    }

    /**
     * Checks costing status
     *
     * @param token - the user token
     * @return request entity
     */
    private RequestEntity checkCostOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/by-id?id=" + costWorkOrderId;

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
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/" + publishWorkOrderId;

        return RequestEntity.init(orderURL, klass)
            .setHeaders(headers)
            .setHeaders(token);
    }

    /**
     * Checks publish result
     *
     * @param token - the user token
     */
    private void checkPublishResult(HashMap<String, String> token) {
        checkOrderSuccessful(checkPublishOrder(token, PublishWorkOrderInfoResult.class));
    }

    /**
     * Checks the order status is successful
     *
     * @param requestEntity - the request entity
     */
    private String checkOrderSuccessful(RequestEntity requestEntity) {
        long initialTime = System.currentTimeMillis() / 1000;
        String requestEntityBody;
        String status;

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        do {
            requestEntityBody = GenericRequestUtil.get(requestEntity, new RequestAreaApi()).getBody();

            status = jsonNode(requestEntityBody, "status");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

        } while ((!status.equals(ORDER_SUCCESS)) && (!status.equals(ORDER_FAILED)) && ((System.currentTimeMillis() / 1000) - initialTime) < WAIT_TIME);

        return requestEntityBody;
    }

    /**
     * Submits the order for processing
     *
     * @param token   - the user token
     * @param orderId - the order id
     */
    private void submitOrder(HashMap<String, String> token, String orderId) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";

        headers.put(CONTENT_TYPE, APPLICATION_JSON);

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
            .setCompType("PART")
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
}