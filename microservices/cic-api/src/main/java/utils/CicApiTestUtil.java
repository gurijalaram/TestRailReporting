package utils;

import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.KeyValueException;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.web.driver.TestBase;

import entity.request.ConnectorRequest;
import entity.request.CostingInputs;
import entity.request.DefaultValues;
import entity.request.JobDefinition;
import entity.request.WorkflowPart;
import entity.request.WorkflowParts;
import entity.request.WorkflowRequest;
import entity.request.WorkflowRow;
import entity.response.AgentConnectionInfo;
import entity.response.AgentConnectionsInfo;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowReportTemplates;
import entity.response.ConnectorInfo;
import entity.response.ConnectorsResponse;
import entity.response.PlmPart;
import entity.response.PlmParts;
import entity.response.ReportTemplatesRow;
import enums.CICAPIEnum;
import enums.CICAgentStatus;
import enums.CICPartSelectionType;
import enums.CICReportType;
import enums.PlmPartsSearch;
import enums.PlmWCType;
import enums.ReportsEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class CicApiTestUtil extends TestBase {

    /**
     * Deserialize workflow data from json file to string.
     *
     * @param fileName - workflow data file name
     * @return fileContent - json string.
     */
    public static String getWorkflowData(String fileName) {
        String fileContent = StringUtils.EMPTY;
        try {
            fileContent = FileResourceUtil.convertFileIntoString(FileResourceUtil.getLocalResourceFile("testdata/" + fileName).getPath());
        } catch (Exception e) {
            log.error("FILE NOT FOUND!!!! " + fileName);
        }
        return fileContent;
    }

    /**
     * De-serialize base workflow requests test data from json file
     *
     * @param cicPartSelectionType Enum CICPartSelectionType (REST or QUERY)
     * @param isCostingInputData   boolean
     * @return WorkFlowRequest builder object
     */
    public static WorkflowRequest getWorkflowBaseData(CICPartSelectionType cicPartSelectionType, Boolean isCostingInputData) {
        WorkflowRequest workflowRequestDataBuilder;
        switch (cicPartSelectionType.getPartSelectionType()) {
            case "REST":
                workflowRequestDataBuilder = (isCostingInputData)
                    ? new TestDataService().getTestData("AgentRestWorkFlowWithCostInputsData.json", WorkflowRequest.class) :
                    new TestDataService().getTestData("AgentRestWorkFlowWithEmptyCostInputData.json", WorkflowRequest.class);

                workflowRequestDataBuilder = setCostingInputData(workflowRequestDataBuilder);
                break;
            default:
                workflowRequestDataBuilder = new TestDataService().getTestData("CicGuiCreateQueryWorkFlowData.json", WorkflowRequest.class);
                workflowRequestDataBuilder = setCostingInputData(workflowRequestDataBuilder);
        }
        return workflowRequestDataBuilder;
    }

    /**
     * De-serialize base Job Definition from json file
     *
     * @return JobDefinition
     */
    public static JobDefinition getJobDefinitionData() {
        return new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
    }

    /**
     * Submit CIC Agent Api request and return response
     *
     * @param endPoint - CICAPIEnum
     * @param klass    - response class name
     * @param <T>      - class object
     * @return response of klass object
     */
    public static <T> ResponseWrapper<T> submitRequest(CICAPIEnum endPoint, Class<T> klass) {
        RequestEntity requestEntity = RequestEntityUtil.init(endPoint, klass)
            .headers(setupHeader())
            .expectedResponseCode(HttpStatus.SC_OK);
        requestEntity.headers(setupHeader());
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit request to get Agent workflow
     *
     * @param workFlowID - id of workflow to get
     * @return response of AgentWorkflow object
     */
    public static AgentWorkflow getCicAgentWorkflow(String workFlowID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW, AgentWorkflow.class)
            .inlineVariables(workFlowID)
            .expectedResponseCode(HttpStatus.SC_OK);
        requestEntity.headers(setupHeader());
        return (AgentWorkflow) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Submit request to get CIC agent workflow jobs
     *
     * @param workFlowID - id of workflow jobs to get
     * @return response
     */
    public static ResponseWrapper<String> getCicAgentWorkflowJobs(String workFlowID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOBS, null)
            .inlineVariables(workFlowID)
            .expectedResponseCode(HttpStatus.SC_OK);
        requestEntity.headers(setupHeader());
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit request to get CIC agent workflow job status
     *
     * @param workFlowID - id of workflow to get job from
     * @param jobID      - id of job to get
     * @return response of AgentWorkflowJob object
     */
    public static AgentWorkflowJob getCicAgentWorkflowJobStatus(String workFlowID, String jobID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_STATUS, AgentWorkflowJob.class)
            .inlineVariables(workFlowID, jobID)
            .expectedResponseCode(HttpStatus.SC_OK);
        requestEntity.headers(setupHeader());
        return (AgentWorkflowJob) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * submit request to run the workflow
     *
     * @param workflowId    workflow identity
     * @param responseClass expected response class
     * @param httpStatus    expected http status code
     * @param <T>           response class type
     * @return object of expected response class
     */
    public static <T> T runCicAgentWorkflow(String workflowId, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_RUN, responseClass)
            .inlineVariables(workflowId)
            .expectedResponseCode(httpStatus);
        requestEntity.headers(setupHeader());
        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Submit CIC thingworx API to create a new workflow
     *
     * @param session      - Login to CI-Connect GUI to get JSession
     * @param workflowData - deserialized workflowdata
     * @return response of created work flow string
     */
    public static ResponseWrapper<String> CreateWorkflow(String session, String workflowData) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_CREATE_WORKFLOW, null)
            .headers(new HashMap<String, String>() {
                {
                    put("Accept", "*/*");
                    put("cookie", String.format("JSESSIONID=%s", session));
                }
            })
            .customBody(workflowData)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Submit CIC thingworx API to create a new workflow
     *
     * @param workflowRequestDataBuilder De-serialized worflow base json data file to WorkflowRequest Builder class
     * @param session                    - Login to CI-Connect GUI to get JSession
     * @return ResponseWrapper<String>
     */
    public static ResponseWrapper<String> CreateWorkflow(WorkflowRequest workflowRequestDataBuilder, String session) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_CREATE_WORKFLOW, null)
            .headers(new HashMap<String, String>() {
                {
                    put("Accept", "*/*");
                    put("cookie", String.format("JSESSIONID=%s", session));
                }
            })
            .body(workflowRequestDataBuilder)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Submit CIC GUI Thingworx API to delete workflow
     *
     * @param session       -  Login to CI-Connect GUI to get JSession
     * @param jobDefinition - deserialized JobDefinition object contains workflowID
     * @return response
     */
    public static ResponseWrapper<String> deleteWorkFlow(String session, JobDefinition jobDefinition) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_DELETE_WORKFLOW, null)
            .headers(initHeadersWithJSession(session))
            .body(jobDefinition)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Submit request to get all CIC agent workflows
     *
     * @return response
     */
    public static ResponseWrapper<String> cancelWorkflow(String workFlowID, String workflowJobID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_CANCEL, null)
            .inlineVariables(workFlowID, workflowJobID)
            .expectedResponseCode(HttpStatus.SC_ACCEPTED);
        requestEntity.headers(setupHeader());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * setup header information for CIC Agent API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setupHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Accept", "application/json");
        header.put("Authorization", PropertiesContext.get("ci-connect.authorization_key"));
        return header;
    }


    /**
     * Submit request to get list of workflows and then get the matched workflow
     *
     * @param workFlowName - WorkflowName
     * @return AgentWorkflow java POJO contains matched workflow information.
     */
    public static AgentWorkflow getMatchedWorkflowId(String workFlowName) {
        AgentWorkflow agentWorkflow = null;
        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        try {
            agentWorkflow = Arrays.stream(agentWorkflows)
                .filter(wf -> wf.getName().equals(workFlowName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find workflow with name " + workFlowName));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return agentWorkflow;
    }

    /**
     * get the customer id based on customer environment name (ant, widgets)
     *
     * @return customer
     */
    public static String getAgent() {
        String agentName = StringUtils.EMPTY;
        try {
            agentName = PropertiesContext.get("${customer}.ci-connect.${${customer}.ci-connect.agent_type}.agent_name");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return agentName;
    }

    /**
     * Track the job status by workflow
     *
     * @param workflowID - workflow id to track status with
     * @param jobID      - job id to track status with
     * @return boolean - true or false (true - Job is in finished state)
     */
    public static Boolean trackWorkflowJobStatus(String workflowID, String jobID) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(15);
        List<String> jobStatusList = Arrays.asList(new String[]{"Finished", "Failed", "Errored", "Cancelled"});
        String finalJobStatus;
        finalJobStatus = getCicAgentWorkflowJobStatus(workflowID, jobID).getStatus();
        while (!jobStatusList.stream().anyMatch(finalJobStatus::contains)) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                return false;
            }
            try {
                TimeUnit.SECONDS.sleep(30);
                finalJobStatus = getCicAgentWorkflowJobStatus(workflowID, jobID).getStatus();
                logger.info(String.format("Job ID  >>%s<< ::: Job Status  >>%s<<", jobID, finalJobStatus));
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }

    /**
     * Track the workflow status until expected agent status is matched
     *
     * @param workflowID     workflow id
     * @param jobID          job id
     * @param cicAgentStatus expected agent status
     * @return boolean true or false
     */
    public static Boolean waitUntilExpectedJobStatusMatched(String workflowID, String jobID, CICAgentStatus cicAgentStatus) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(2);
        String finalJobStatus;
        finalJobStatus = getCicAgentWorkflowJobStatus(workflowID, jobID).getStatus();
        while (!finalJobStatus.contains(cicAgentStatus.getAgentStatus())) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                return false;
            }
            try {
                TimeUnit.SECONDS.sleep(30);
                finalJobStatus = getCicAgentWorkflowJobStatus(workflowID, jobID).getStatus();
                logger.info(String.format("Job ID  >>%s<< ::: Job Status  >>%s<<", jobID, finalJobStatus));
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }

    /**
     * submit run parts list api for cic agent workflow
     *
     * @param workflowId workflow Identity
     * @return response class object
     */
    public static <T> T runCicAgentWorkflowPartList(String workflowId, WorkflowParts workflowPartsRequestBuilder, Class<T> responseClass, Integer expectedHttpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_RUN_PARTS_LIST, responseClass)
            .inlineVariables(workflowId)
            .headers(setupHeader())
            .body(workflowPartsRequestBuilder)
            .expectedResponseCode(expectedHttpStatus);

        if (null != responseClass) {
            return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
        } else {
            return (T) HTTPRequest.build(requestEntity).post();
        }
    }

    /**
     * Submit request to get CIC agent Workflow job results
     *
     * @param workFlowIdentity workflow identity
     * @param jobIdentity      Workflow job Identity
     * @param responseClass    expected response class
     * @param httpStatus       expected http status code
     * @param <T>              expected response type
     * @return response class type
     */
    public static <T> T getCicAgentWorkflowJobResult(String workFlowIdentity, String jobIdentity, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_RESULT, responseClass)
            .inlineVariables(workFlowIdentity, jobIdentity)
            .expectedResponseCode(httpStatus);
        requestEntity.headers(setupHeader());

        if (null != responseClass) {
            return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
        } else {
            return (T) HTTPRequest.build(requestEntity).get();
        }
    }

    /**
     * Submit request to get CIC agent Workflow job Parts results
     *
     * @param workFlowIdentity Workflow Identity
     * @param jobIdentity      Workflow Job Identity
     * @param partIdentity     workflow Job Part identity
     * @param responseClass    expected response class
     * @param httpStatus       expected http status code
     * @param <T>              expected response type
     * @return response class type
     */
    public static <T> T getCicAgentWorkflowJobPartsResult(String workFlowIdentity, String jobIdentity, String partIdentity, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_PART_RESULT, responseClass)
            .inlineVariables(workFlowIdentity, jobIdentity, partIdentity)
            .expectedResponseCode(httpStatus);
        requestEntity.headers(setupHeader());
        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * get the customer id based on customer environment name (ant, widgets)
     *
     * @return customer
     */
    public static String getCustomerName() {
        return PropertiesContext.get("${customer}.ci-connect.customer_name");
    }

    /**
     * This method execute PLM Windchill Search REST API
     * to search with partial part name or part number
     *
     * @param searchFilter SearchFilter ( created sample tests for usage)
     * @return PlmParts Response object
     */
    public static PlmParts searchPlmWindChillParts(SearchFilter searchFilter) {
        PlmPart plmPart = null;
        QueryParams queryParams = new QueryParams();
        List<String[]> paramKeyValue = Arrays.stream(searchFilter.getQueryParams()).map(o -> o.split(":")).collect(Collectors.toList());
        Map<String, String> paramMap = new HashMap<>();
        try {
            paramKeyValue.forEach(o -> paramMap.put(o[0].trim(), o[1].trim()));
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new KeyValueException(ae.getMessage(), paramKeyValue);
        }
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_PLM_WC_SEARCH, PlmParts.class).queryParams(queryParams.use(paramMap)).headers(new HashMap<String, String>() {
            {
                put("Authorization", "Basic " + PropertiesContext.get("ci-connect.${ci-connect.agent_type}.host_token"));
            }
        }).expectedResponseCode(HttpStatus.SC_OK);

        return (PlmParts) HTTPRequest.build(requestEntity).get().getResponseEntity();

    }

    /**
     * search and return single Plm Windchill Part
     *
     * @param searchFilter
     * @return PlmPart response class
     */
    @SneakyThrows
    public static PlmPart getPlmPart(SearchFilter searchFilter) {
        PlmParts plmParts = searchPlmWindChillParts(searchFilter);
        PlmPart plmPart;
        try {
            if (plmParts.getItems().size() > 1) {
                plmPart = plmParts
                    .getItems()
                    .get(new Random().nextInt(plmParts.getItems().size()));
            } else {
                plmPart = plmParts.getItems().get(0);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return plmPart;
    }

    /**
     * Build workflow run parts request data builder
     *
     * @param partData   PartData class
     * @param numOfParts - number of parts to build
     * @return WorkflowParts data builder class
     */
    public static WorkflowParts getWorkflowPartDataBuilder(PartData partData, Integer numOfParts) {
        PlmParts plmParts;
        ArrayList<WorkflowPart> part = null;
        try {
            plmParts = CicApiTestUtil.searchPlmWindChillParts(new SearchFilter()
                .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() +
                    (numOfParts > 5 ? String.format(PlmPartsSearch.PLM_WC_PART_NAME_ENDS_WITH.getFilterKey(), "prt") : String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(), partData.getPlmPartNumber())))
                .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
                .build());

            List<PartData> partDataList = PlmPartsUtil.getPlmPartsFromCloud(numOfParts);
            part = new ArrayList<>();
            for (int i = 0; i < numOfParts; i++) {
                part.add(WorkflowPart.builder()
                    .id(plmParts.getItems().get(i).getId())
                    .costingInputs(CostingInputs.builder()
                        .processGroupName(partDataList.get(i).getProcessGroup())
                        .materialName(partDataList.get(i).getMaterial())
                        .vpeName(partDataList.get(i).getDigitalFactory())
                        .scenarioName("SN" + System.currentTimeMillis())
                        .annualVolume(partDataList.get(i).getAnnualVolume())
                        .batchSize(partDataList.get(i).getBatchSize())
                        .build())
                    .build());
            }
        } catch (Exception e) {
            logger.error("PARTS NOT FOUND IN PLM SYSTEM WITH PART NUMBER --- " + partData.getPlmPartNumber());
            throw new IllegalArgumentException(e);
        }

        return WorkflowParts.builder()
            .parts(part)
            .build();
    }

    /**
     * Verify the message from list of job parts result
     *
     * @param agentWorkflowJobResults AgentWorkflowJobResults
     * @param errorMessage            expected message
     * @return boolean
     */
    public static Boolean verifyAgentErrorMessage(AgentWorkflowJobResults agentWorkflowJobResults, String errorMessage) {
        Boolean isTextMatched = false;
        try {
            for (AgentWorkflowJobPartsResult result : agentWorkflowJobResults) {
                if (null != result.getErrorMessage()) {
                    if (result.getErrorMessage().contains(errorMessage)) {
                        isTextMatched = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return isTextMatched;
    }

    /**
     * Get Connector request base data based on CICAgentType (Windchill, Team_Center or File_system)
     *
     * @return ConnectorRequest java class object
     */
    public static ConnectorRequest getConnectorBaseData() {
        ConnectorRequest connectorRequestDataBuilder = null;
        switch (PropertiesContext.get("ci-connect.agent_type")) {
            case "teamcenter":
                connectorRequestDataBuilder = new TestDataService().getTestData("ConnectorTeamCenterData.json", ConnectorRequest.class);
                connectorRequestDataBuilder.setCustomer(getCustomerName());
                break;
            default:
                connectorRequestDataBuilder = new TestDataService().getTestData("ConnectorWindchillData.json", ConnectorRequest.class);
                connectorRequestDataBuilder.setCustomer(getCustomerName());
        }
        return connectorRequestDataBuilder;
    }

    /**
     * Create Connector using Thingworx create agent api
     *
     * @param connectorRequestDataBuilder ConnectorRequest request data builder object
     * @param session                     - Login session
     * @return ResponseWrapper of String
     */
    public static ResponseWrapper<String> CreateConnector(ConnectorRequest connectorRequestDataBuilder, String session) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        header.put("cookie", String.format("JSESSIONID=%s", session));
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_CREATE_CONNECTOR, null)
            .headers(header)
            .body(connectorRequestDataBuilder)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Get matched connector from list of get connectors API
     *
     * @param connectorName - expected connector Name
     * @param session       Login session
     * @return ConnectorInfo response class object
     */
    @SneakyThrows
    public static ConnectorInfo getMatchedConnector(String connectorName, String session) {
        ConnectorInfo connectorInfo = null;
        String getConnectorJson = String.format("{\"customerThingName\": \"%s\"}", getCustomerName());
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_GET_CONNECTORS, ConnectorsResponse.class)
            .headers(initHeadersWithJSession(session))
            .customBody(getConnectorJson)
            .expectedResponseCode(HttpStatus.SC_OK);
        ConnectorsResponse connectorResponse = (ConnectorsResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
        try {
            connectorInfo = connectorResponse.getRows().stream()
                .filter(conn -> conn.getDisplayName().equals(connectorName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find connector with name " + connectorName));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return connectorInfo;
    }

    /**
     * Get connector connection info contains appKey and wss URL options
     *
     * @param connectorIdentity connector name
     * @param session           login session
     * @return AgentConnectionInfo
     */
    public static AgentConnectionInfo getAgentConnectorOptions(String connectorIdentity, String session) {
        String getConnectorJson = String.format("{\"connectorName\": \"%s\"}", connectorIdentity);
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_GET_AGENT_CONNECTION_INFO, AgentConnectionsInfo.class)
            .headers(initHeadersWithJSession(session))
            .customBody(getConnectorJson)
            .expectedResponseCode(HttpStatus.SC_OK);
        AgentConnectionsInfo agentConnectionInfo = (AgentConnectionsInfo) HTTPRequest.build(requestEntity).post().getResponseEntity();
        return agentConnectionInfo.getRows().get(0);
    }

    /**
     * get Workflow Report template names
     *
     * @param cicReportType EMAIL or PLM_WRITE
     * @param session       login session
     * @return ReportTemplatesRow
     */
    public static AgentWorkflowReportTemplates getAgentReportTemplates(CICReportType cicReportType, String session) {
        String getConnectorJson = String.format("{\"customer\":\"%s\",\"reportType\":\"%s\"}", getCustomerName(), cicReportType);
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_GET_WORKFLOW_REPORT_TEMPLATES, AgentWorkflowReportTemplates.class)
            .headers(initHeadersWithJSession(session))
            .customBody(getConnectorJson)
            .expectedResponseCode(HttpStatus.SC_OK);
        return (AgentWorkflowReportTemplates) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * get the report template id
     *
     * @param agentWorkflowReportTemplates list of templates
     * @param reportName                   expected report name
     * @return ReportTemplatesRow single template object
     */
    public static ReportTemplatesRow getAgentReportTemplate(AgentWorkflowReportTemplates agentWorkflowReportTemplates, ReportsEnum reportName) {
        ReportTemplatesRow reportTemplate = null;
        try {
            reportTemplate = agentWorkflowReportTemplates.getRows().stream()
                .filter(conn -> conn.getDisplayName().equals(reportName.getReportName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find report template with name " + reportName.getReportName()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return reportTemplate;
    }

    /**
     * set costing put data for workflow data request builder
     *
     * @param workflowRequestDataBuilder
     * @return WorkflowRequest
     */
    private static WorkflowRequest setCostingInputData(WorkflowRequest workflowRequestDataBuilder) {
        DefaultValues defaultValues = workflowRequestDataBuilder.getDefaultValues();
        if (null != defaultValues) {
            List<WorkflowRow> rows = defaultValues.getRows();
            rows.stream().forEach(row -> {
                if (row.getTwxAttributeName().equals("Scenario Name")) {
                    row.setValue("SN" + System.currentTimeMillis());
                }
            });
            defaultValues.setRows(rows);
            workflowRequestDataBuilder.setDefaultValues(defaultValues);
        }
        workflowRequestDataBuilder.setCustomer(getCustomerName());
        workflowRequestDataBuilder.setPlmSystem(getAgent());
        workflowRequestDataBuilder.setName("CIC" + System.currentTimeMillis());
        workflowRequestDataBuilder.setDescription(new GenerateStringUtil().getRandomString());
        return workflowRequestDataBuilder;
    }

    /**
     * setup header information for CIC Agent API Cookie
     *
     * @param session
     * @return Map
     */
    private static Map<String, String> initHeadersWithJSession(String session) {
        return new HashMap<String, String>() {
            {
                put("Accept", "*/*");
                put("cookie", String.format("JSESSIONID=%s", session));
                put("Accept", "application/json");
            }
        };
    }

}