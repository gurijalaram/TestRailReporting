package com.apriori.utils;

import com.apriori.apibase.services.cid.objects.cost.FileOrderResponse;
import com.apriori.apibase.services.cid.objects.cost.costworkorderstatus.EmptyCostWorkOrderInfo;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.Command;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.Command_;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.Inputs;
import com.apriori.apibase.services.cid.objects.cost.createcostworkorder.ScenarioIterationKey;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.MaterialBean;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ProductionInfo;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ScenarioKey;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.ScenarioKey_;
import com.apriori.apibase.services.cid.objects.cost.productioninfo.VpeBean;
import com.apriori.apibase.services.cid.objects.upload.FileCommandEntity;
import com.apriori.apibase.services.cid.objects.upload.FileOrderEntity;
import com.apriori.apibase.services.cid.objects.upload.FileOrdersEntity;
import com.apriori.apibase.services.cid.objects.upload.FileUploadWorkOrder;
import com.apriori.apibase.services.cid.objects.upload.FileWorkOrderEntity;
import com.apriori.apibase.services.cis.objects.requests.NewPartRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileUploadResources {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadResources.class);

    private static String identity;
    private static String orderId;
    private static int costOrderId;
    private static String stateName;
    private static int workspaceId;
    private static String typeName;
    private static String masterName;
    private static int iteration;
    private static String costWorkOrderId;
    Map<String, String> headers = new HashMap<>();

    private String contentType = "Content-Type";
    private String applicationJson = "application/json";


    public void createFileUpload(HashMap<String, String> token, Object fileObject) {
        initializeFileUpload(token, fileObject);
        createFileUploadWorkOrder(token, fileObject);
        submitFileUploadWorkOrder(token);
        checkFileWorkOrderStatus(token);
        initializeCostScenario(token);
        createCostWorkOrder(token);
        submitCostWorkOrder(token);
        checkCostWorkOrderStatus(token);
        checkCostResult(token);
    }

    private void initializeFileUpload(HashMap<String, String> token, Object fileObject) {
        NewPartRequest npr = (NewPartRequest) fileObject;
        String url = Constants.getBaseUrl() + "apriori/cost/session/ws/files";

        headers.put(contentType, "multipart/form-data");

        RequestEntity requestEntity = RequestEntity.init(url, FileResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getResourceAsFile(npr.getFilename())))
            .setFormParams(new FormParams().use("filename", npr.getFilename()));

        identity = jsonNode(GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getBody(), "identity");
    }

    private void createFileUploadWorkOrder(HashMap<String, String> token, Object fileObject) {
        NewPartRequest npr = (NewPartRequest) fileObject;
        String fileURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity fileRequestEntity = RequestEntity.init(fileURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileCommandEntity()
                .setCommand(new FileOrdersEntity()
                    .setCommandType("LOADCADFILE")
                    .setInputs(new FileOrderEntity().setScenarioName(npr.getScenarioName())
                        .setFileKey(identity)
                        .setFileName(npr.getFilename()))));

        orderId = jsonNode(GenericRequestUtil.post(fileRequestEntity, new RequestAreaApi()).getBody(), "id");
    }

    private void submitFileUploadWorkOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList(orderId))
                .setAction("SUBMIT"));

        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
    }

    private void checkFileWorkOrderStatus(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/" + orderId;

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, FileUploadWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token);

        long startTime = System.currentTimeMillis() / 1000;

        do {
            jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status");
        } while ((!jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status").equals("SUCCESS")) &&
            (!jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status").equals("FAILED")) &&
            ((System.currentTimeMillis() / 1000) - startTime) < 240);

        String orderBody = GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody();

        workspaceId = Integer.parseInt(jsonNode(orderBody, "workspaceId"));
        typeName = jsonNode(orderBody, "typeName");
        masterName = jsonNode(orderBody, "masterName");
        stateName = jsonNode(orderBody, "stateName");
        iteration = Integer.parseInt(jsonNode(orderBody, "iteration"));
    }

    private void initializeCostScenario(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + masterName + "/" + stateName + "/iterations/" + iteration + "/production-info";

        headers.put(contentType, applicationJson);

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(
                new ProductionInfo()
                    .setScenarioKey(new ScenarioKey().setWorkspaceId(workspaceId)
                        .setTypeName(typeName)
                        .setStateName(stateName)
                        .setMasterName(masterName))
                    .setCompType("PART")
                    .setInitialized(false)
                    .setAvailablePgNames(Arrays.asList("2-Model Machining", "Additive Manufacturing", "Bar & Tube Fab", "Casting", "Casting - Die", "Casting - Sand",
                        "Forging", "Plastic Molding", "Powder Metal", "Rapid Prototyping", "Roto & Blow Molding", "Sheet Metal",
                        "Sheet Metal - Hydroforming", "Sheet Metal - Stretch Forming", "Sheet Metal - Transfer Die",
                        "Sheet Plastic", "Stock Machining", "User Guided"))

                    .setProcessGroupName("Casting - Die")
                    .setPgEnabled(true)

                    .setVpeBean(new VpeBean()
                        .setScenarioKey(new ScenarioKey_().setWorkspaceId(workspaceId)
                            .setTypeName(typeName)
                            .setStateName(stateName)
                            .setMasterName(masterName))

                        .setPrimaryPgName("Casting - Die")
                        .setPrimaryVpeName("aPriori USA")
                        .setAutoSelectedSecondaryVpes(null)
                        .setUsePrimaryAsDefault(true)
                        .setInitialized(false)

                        .setMaterialCatalogKeyData(new MaterialCatalogKeyData().setFirst("aPriori USA")
                            .setSecond("Casting - Die")))

                    .setSupportsMaterials(true)
                    .setMaterialBean(new MaterialBean().setInitialized(false)
                        .setVpeDefaultMaterialName("Aluminum, Cast, ANSI AL380.0")
                        .setMaterialMode("CAD")
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
                    .setAvailableCurrencyCodes(Arrays.asList("USD"))
                    .setManualCurrencyCode("USD")
                    .setMachiningMode("MAY_BE_MACHINED")
                    .setHasTargetCost(false)
                    .setHasTargetFinishMass(null)
                    .setHasTargetFinishMass(false)
                    .setCadModelLoaded(true)
                    .setThicknessVisible(true));

        costOrderId = Integer.parseInt(jsonNode(GenericRequestUtil.post(costRequestEntity, new RequestAreaApi()).getBody(), "id"));
    }

    private void createCostWorkOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, FileOrderResponse.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new Command_().setCommand(new Command()
                .setCommandType("COSTING")
                .setInputs(new Inputs().setInputSetId(costOrderId)
                    .setScenarioIterationKey(new ScenarioIterationKey().setIteration(iteration)
                        .setScenarioKey(new ScenarioKey().setMasterName(masterName)
                            .setStateName(stateName)
                            .setTypeName(typeName)
                            .setWorkspaceId(workspaceId))))));

        costWorkOrderId = jsonNode(GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody(),"id");
    }

    private void submitCostWorkOrder(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";

        headers.put(contentType, applicationJson);

        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
            .setHeaders(headers)
            .setHeaders(token)
            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList(costWorkOrderId))
                .setAction("SUBMIT"));

        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
    }

    private void checkCostWorkOrderStatus(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/by-id?id=" + costWorkOrderId;

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, EmptyCostWorkOrderInfo.class)
            .setHeaders(headers)
            .setHeaders(token);

        long startTime = System.currentTimeMillis() / 1000;

        String status;
        do {
            status = jsonNode(GenericRequestUtil.get(costRequestEntity, new RequestAreaApi()).getBody(), "status");
        } while ((!status.equals("PROCESSING")) &&
            (!status.equals("FAILED")) &&
            ((System.currentTimeMillis() / 1000) - startTime) < 60);
    }

    private void checkCostResult(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders/by-id?id=" + costWorkOrderId;

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, EmptyCostWorkOrderInfo.class)
            .setHeaders(headers)
            .setHeaders(token);

        long startTime = System.currentTimeMillis() / 1000;

        String status;
        do {
            status = jsonNode(GenericRequestUtil.get(costRequestEntity, new RequestAreaApi()).getBody(), "status");
        } while ((!status.equals("SUCCESS")) &&
            (!status.equals("FAILED")) &&
            ((System.currentTimeMillis() / 1000) - startTime) < 60);
    }

    private void costingIteration(HashMap<String, String> token) {
        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workspace/" + workspaceId + "/scenarios/" + typeName + "/" + masterName + "/" + stateName + "/iterations";

        RequestEntity costRequestEntity = RequestEntity.init(orderURL, EmptyCostWorkOrderInfo.class)
            .setHeaders(headers)
            .setHeaders(token);
    }




//    private void publishWorkOrder(HashMap<String, String> token) {
//        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orders";
//
//        headers.put(contentType, applicationJson);
//
//        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, FileUploadWorkOrder.class)
//            .setHeaders(headers)
//            .setHeaders(token)
//            .setBody();
//    }
//
//    private void submitPublishWorkOrder(HashMap<String, String> token) {
//        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus";
//
//        headers.put(contentType, applicationJson);
//
//        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
//            .setHeaders(headers)
//            .setHeaders(token)
//            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList())
//                .setAction("SUBMIT"));
//
//        GenericRequestUtil.post(orderRequestEntity, new RequestAreaApi()).getBody();
//    }
//
//    private void checkPublishWorkOrder(HashMap<String, String> token) {
//        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/orderstatus" + publishOrderId;
//
//        headers.put(contentType, applicationJson);
//
//        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
//            .setHeaders(headers)
//            .setHeaders(token)
//            .setBody(new FileWorkOrderEntity().setOrderIds(Collections.singletonList())
//                .setAction("SUBMIT"));
//
//        long startTime = System.currentTimeMillis() / 1000;
//
//        do {
//            jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status");
//        } while ((!jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status").equals("SUCCESS")) &&
//            (!jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "status").equals("FAILED")) &&
//            ((System.currentTimeMillis() / 1000) - startTime) < 60);
//    }
//
//    private void checkPublishWorkOrderDB(HashMap<String, String> token) {
//        String orderURL = Constants.getBaseUrl() + "apriori/cost/session/ws/workorder/order"+ publishOrderId;
//
//        headers.put(contentType, applicationJson);
//
//        RequestEntity orderRequestEntity = RequestEntity.init(orderURL, SubmitWorkOrder.class)
//            .setHeaders(headers)
//            .setHeaders(token);
//
//            jsonNode(GenericRequestUtil.get(orderRequestEntity, new RequestAreaApi()).getBody(), "statusCode");
//    }

    private String jsonNode(String jsonProperties, String path) {
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(jsonProperties);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new NullPointerException("can't read json node");
        }
        return node.findPath(path).asText();
    }
}
