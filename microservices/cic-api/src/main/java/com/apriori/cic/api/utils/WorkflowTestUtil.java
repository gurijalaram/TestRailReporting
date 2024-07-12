package com.apriori.cic.api.utils;

import com.apriori.cic.api.enums.CICAPIEnum;
import com.apriori.cic.api.models.request.WorkflowParts;
import com.apriori.cic.api.models.request.WorkflowRequest;
import com.apriori.cic.api.models.response.AgentWorkflow;
import com.apriori.cic.api.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.models.response.ConnectorJobPart;
import com.apriori.cic.api.models.response.ConnectorJobParts;
import com.apriori.shared.util.file.PDFDocument;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.models.response.EmailMessageAttachments;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class WorkflowTestUtil extends CicUtil {

    private static final int WAIT_TIME = 30;
    protected AgentWorkflow agentWorkflowResponse;
    protected ResponseWrapper<String> workflowResponse;
    protected AgentWorkflowJobRun agentWorkflowJobRunResponse;
    protected WorkflowRequest workflowRequestDataBuilder;
    protected WorkflowParts workflowPartsRequestDataBuilder;
    protected CicLoginUtil cicLoginUtil;
    protected PartData plmPartData;
    protected UserCredentials currentUser;

    /**
     * Create workflow
     *
     * @param workflowRequestData WorkflowRequest
     * @param sessionID           JSessionID
     * @return current class object
     */
    public WorkflowTestUtil createWorkflow(WorkflowRequest workflowRequestData, String sessionID) {
        workflowResponse = CicApiTestUtil.createWorkflow(workflowRequestData, sessionID);
        if (workflowResponse == null) {
            throw new RuntimeException("Workflow creation failed!!");
        }
        if (workflowResponse.getBody().contains("CreateJobDefinition") && workflowResponse.getBody().contains(">true<")) {
            log.debug(String.format("WORKFLOW CREATED SUCCESSFULLY (%s)", workflowRequestData.getName()));
        }
        return this;
    }

    /**
     * Get matching workflow from list of returned workflows
     *
     * @param workflowName Workflow name
     * @return Current class object
     */
    public WorkflowTestUtil getWorkflowId(String workflowName) {
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        if (agentWorkflowResponse == null) {
            throw new RuntimeException("FAILED TO FIND WORKFLOW!!!");
        }
        return this;
    }

    /**
     * Get matching workflow from list of returned workflows
     *
     * @return Current class object
     */
    public WorkflowTestUtil getWorkflowId() {
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(this.workflowRequestDataBuilder.getName());
        if (agentWorkflowResponse == null) {
            throw new RuntimeException("FAILED TO FIND WORKFLOW!!!");
        }
        return this;
    }

    /**
     * Invoke workflow
     *
     * @return Current class object
     */
    public WorkflowTestUtil invokeQueryWorkflow() {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        if (agentWorkflowJobRunResponse == null) {
            throw new RuntimeException("FAILED TO INVOKE WORKFLOW!!!");
        }
        return this;
    }

    /**
     * Invoke Workflow of Part Selection Type REST with number of parts as parameter
     *
     * @param workflowParts WorkflowParts
     * @return WorkflowTestUtil
     */
    public WorkflowTestUtil invokeRestWorkflow(WorkflowParts workflowParts) {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            workflowParts,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        return this;
    }

    /**
     * Invoke Workflow of Part Selection Type REST with number of parts
     *
     * @return WorkflowTestUtil
     */
    public WorkflowTestUtil invokeRestWorkflow() {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflowPartList(
            agentWorkflowResponse.getId(),
            this.workflowPartsRequestDataBuilder,
            AgentWorkflowJobRun.class,
            HttpStatus.SC_OK);
        return this;
    }

    /**
     * Track workflow job
     *
     * @return Current class object
     */
    public WorkflowTestUtil track() {
        if (!CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(), cicLoginUtil)) {
            throw new RuntimeException("FAILED TO FINISH WORKFLOW TO A TERMINAL STATE!!!");
        }
        return this;
    }

    /**
     * Track workflow job
     *
     * @return Current class object
     */
    public WorkflowTestUtil trackWorkflowWithoutRefresh() {
        if (!CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())) {
            throw new RuntimeException("FAILED TO FINISH WORKFLOW TO A TERMINAL STATE!!!");
        }
        return this;
    }

    /**
     * get workflow job results
     *
     * @return Current class object
     */
    public AgentWorkflowJobResults getJobResult() {
        AgentWorkflowJobResults agentWorkflowJobResults = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);
        if (agentWorkflowJobResults.get(0).getInput() == null) {
            log.debug(agentWorkflowJobResults.get(0).getErrorMessage());
        }
        return agentWorkflowJobResults;
    }

    /**
     * get workflow job part results
     *
     * @return Current class object
     */
    public AgentWorkflowJobPartsResult getJobPartResult(String partId) {
        return CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            partId,
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);
    }

    /**
     * Create workflow and get job results after processing the workflow
     *
     * @param workflowRequestData workflowRequestData
     * @param sessionID           JSESSIONID
     * @return AgentWorkflowJobResults
     */
    public AgentWorkflowJobResults createWorkflowAndGetJobResult(WorkflowRequest workflowRequestData, String sessionID) {
        return createWorkflow(workflowRequestData, sessionID)
            .getWorkflowId(workflowRequestData.getName())
            .invokeQueryWorkflow()
            .track()
            .getJobResult();
    }

    /**
     * login CIC Login
     *
     * @return current class object
     */
    public WorkflowTestUtil cicLogin() {
        this.cicLoginUtil = new CicLoginUtil(this.driver).login(currentUser);
        return this;
    }

    /**
     * Create workflow
     *
     * @return current class object
     */
    public WorkflowTestUtil create() {
        this.workflowRequestDataBuilder.setCustomer(CicApiTestUtil.getCustomerName());
        this.workflowRequestDataBuilder.setPlmSystem(CicApiTestUtil.getAgent(this.cicLoginUtil.getSessionId()));
        workflowResponse = CicApiTestUtil.createWorkflow(this.workflowRequestDataBuilder, cicLoginUtil.getSessionId());
        if (workflowResponse == null) {
            throw new RuntimeException("Workflow creation failed!!");
        }
        if (workflowResponse.getBody().contains("CreateJobDefinition") && workflowResponse.getBody().contains(">true<")) {
            log.debug(String.format("WORKFLOW CREATED SUCCESSFULLY (%s)", this.workflowRequestDataBuilder.getName()));
        }
        return this;
    }

    /**
     * delete workflow
     *
     * @return current class object
     */
    public WorkflowTestUtil deleteWorkflow() {
        if (this.workflowRequestDataBuilder != null) {
            CicApiTestUtil.deleteWorkFlow(this.cicLoginUtil.getSessionId(), CicApiTestUtil.getMatchedWorkflowId(this.workflowRequestDataBuilder.getName()));
        }
        return this;
    }

    /**
     * Create workflow of Query Definition type 'REST', process and get job result
     *
     * @return AgentWorkflowJobResults
     */
    public AgentWorkflowJobResults createRestWorkflowAndGetJobResult() {
        return this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track()
            .getJobResult();
    }

    /**
     * Create workflow of Query Definition type 'QUERY', process and get job result
     *
     * @return AgentWorkflowJobResults
     */
    public AgentWorkflowJobResults createQueryWorkflowAndGetJobResult() {
        return this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobResult();
    }

    /**
     * Create workflow of Query Definition type 'REST', Process workflow and get job part results
     *
     * @return AgentWorkflowJobPartsResult
     */
    public AgentWorkflowJobPartsResult createRestWorkflowAndGetJobPartResult() {
        return this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track()
            .getJobPartResult(this.workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    /**
     * Close response objects part of clean up ofter completion of test execution
     */
    public void close() {
        this.agentWorkflowResponse = null;
        this.workflowResponse = null;
        this.agentWorkflowJobRunResponse = null;
        this.workflowRequestDataBuilder = null;
        this.workflowPartsRequestDataBuilder = null;
    }

    /**
     * getter for AgentWorkflowJobRun class object
     *
     * @return AgentWorkflowJobRun
     */
    public AgentWorkflowJobRun getAgentWorkflowJobRunResponse() {
        return agentWorkflowJobRunResponse;
    }

    /**
     * delete workflows
     *
     * @return current class object
     */
    public Boolean isWorkflowsDeleted() {
        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        try {
            Arrays.stream(agentWorkflows)
                .filter(wf -> wf.getName().startsWith("CIC") || wf.getName().startsWith("-"))
                .forEach(wf -> CicApiTestUtil.deleteWorkFlow(this.cicLoginUtil.getSessionId(), wf));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        ResponseWrapper<String> workflowResponse = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] workflowList = JsonManager.deserializeJsonFromString(workflowResponse.getBody(), AgentWorkflow[].class);
        List<AgentWorkflow> agentWorkflow = Arrays.stream(workflowList)
            .filter(wf -> wf.getName().startsWith("CIC") || wf.getName().startsWith("-"))
            .collect(Collectors.toList());

        return (agentWorkflow.size() > 0) ? false : true;
    }

    /**
     * Verify PLM part name in job results
     *
     * @param agentWorkflowJobResults - AgentWorkflowJobResults
     * @param plmPartData             - Expected PartData
     * @return boolean
     */
    public Boolean verifyPartNameInJobResult(AgentWorkflowJobResults agentWorkflowJobResults, List<PartData> plmPartData) {
        return agentWorkflowJobResults.stream()
            .anyMatch(agentWorkflowJobPartsResult ->
                plmPartData.stream()
                    .peek(partData -> {
                        if (agentWorkflowJobPartsResult.getCidPartNumber().contains(partData.getPartName())) {
                            log.debug(String.format("ACTUAL PART NAME  : (%s) <=> EXPECTED PART NAME : (%s)", agentWorkflowJobPartsResult.getCidPartNumber(), partData.getPartName()));
                        }
                    })
                    .anyMatch(partData ->
                        agentWorkflowJobPartsResult.getCidPartNumber().contains(partData.getPartName())
                    )
            );
    }

    /**
     * Verify PLM part number in job results
     *
     * @param agentWorkflowJobResults - AgentWorkflowJobResults
     * @param plmPartData             - Expected PartData
     * @return boolean
     */
    public Boolean verifyPartNumberInJobResult(AgentWorkflowJobResults agentWorkflowJobResults, List<PartData> plmPartData) {
        return agentWorkflowJobResults.stream()
            .anyMatch(agentWorkflowJobPartsResult ->
                plmPartData.stream()
                    .peek(partData -> {
                        if (agentWorkflowJobPartsResult.getPartNumber().contains(partData.getPlmPartNumber())) {
                            log.debug(String.format("ACTUAL PART NUMBER  : (%s) <=> EXPECTED PART NUMBER : (%s)", agentWorkflowJobPartsResult.getPartNumber(), partData.getPlmPartNumber()));
                        }
                    })
                    .anyMatch(partData ->
                        agentWorkflowJobPartsResult.getPartNumber().contains(partData.getPlmPartNumber())
                    )
            );
    }

    /**
     * verify CID part number in PDF report
     *
     * @param emailMessageAttachments - EmailMessageAttachments
     * @param agentWorkflowJobResults - AgentWorkflowJobResults
     * @return Boolean
     */
    public Boolean verifyJobPartsInPdfReport(EmailMessageAttachments emailMessageAttachments, AgentWorkflowJobResults agentWorkflowJobResults) {
        return emailMessageAttachments.value.stream()
            .map(emailMessageAttachment -> (PDFDocument) emailMessageAttachment.getFileAttachment())
            .anyMatch(pdfDocument ->
                agentWorkflowJobResults.stream()
                    .peek(workflowJobPartsResult -> {
                        if (!pdfDocument.getDocumentContents().contains(workflowJobPartsResult.getCidPartNumber())) {
                            log.debug(String.format("ACTUAL Document content : (%s) <=> EXPECTED CID PART Number : (%s)", pdfDocument.getDocumentContents(), workflowJobPartsResult.getCidPartNumber()));
                        }
                    })
                    .anyMatch(workflowJobPartsResult ->
                        pdfDocument.getDocumentContents().contains(workflowJobPartsResult.getCidPartNumber())
                    )
            );
    }

    /**
     * Verify Report in received email report
     *
     * @param emailMessageAttachments - AgentWorkflowJobResults
     * @param plmPartData             - Expected PartData
     * @return boolean
     */
    public Boolean verifyEmailAttachedReportName(EmailMessageAttachments emailMessageAttachments, List<PartData> plmPartData) {
        return emailMessageAttachments.value.stream()
            .anyMatch(emailMessageAttachment ->
                plmPartData.stream()
                    .peek(partData -> {
                        if (!emailMessageAttachment.getName().contains(partData.getPartName())) {
                            log.debug(String.format("ACTUAL REPORT NAME  : (%s) <=> EXPECTED REPORT NAME : (%s)", emailMessageAttachment.getName(), partData.getPartName()));
                        }
                    })
                    .anyMatch(partData ->
                        emailMessageAttachment.getName().contains(partData.getPartName())
                    )
            );
    }

    /**
     * Verify Report content in email (PDF document)
     *
     * @param emailMessageAttachments
     * @param plmPartData
     * @return boolean
     */
    public Boolean verifyPdfDocumentContent(EmailMessageAttachments emailMessageAttachments, List<PartData> plmPartData) {
        return emailMessageAttachments.value.stream()
            .map(emailMessageAttachment -> (PDFDocument) emailMessageAttachment.getFileAttachment())
            .anyMatch(pdfDocument ->
                plmPartData.stream()
                    .peek(partData -> {
                        if (!pdfDocument.getDocumentContents().contains(partData.getPartName().toUpperCase())) {
                            log.debug(String.format("ACTUAL Document content : (%s) <=> EXPECTED PART NAME : (%s)", pdfDocument.getDocumentContents(), partData.getPartName()));
                        }
                    })
                    .anyMatch(partData ->
                        pdfDocument.getDocumentContents().contains(partData.getPartName().toUpperCase())
                    )
            );
    }

    /**
     * verify data in pdf document
     *
     * @param pdfDocument        - PDFDocument
     * @param expectedPdfContent - expected pdf content
     * @return Boolean
     */
    public Boolean verifyPdfDocumentContent(PDFDocument pdfDocument, List<String> expectedPdfContent) {
        String pdfContent = pdfDocument.getDocumentContents();
        return expectedPdfContent.stream()
            .peek(item -> {
                if (!pdfContent.contains(item)) {
                    log.debug(String.format("ACTUAL Email content : (%s) <=> EXPECTED Content  : (%s)", pdfContent, item));
                }
            })
            .allMatch(pdfContent::contains);
    }

    /**
     * verify Email Body Content
     *
     * @param emailMessage EmailMessage object
     * @param contentList  - expected content list
     * @return Boolean
     */
    public Boolean verifyEmailBody(EmailMessage emailMessage, List<String> contentList) {
        String emailContent = emailMessage.getBody().getContent();
        return contentList.stream()
            .peek(item -> {
                if (!emailContent.contains(item)) {
                    log.debug(String.format("ACTUAL Email content : (%s) <=> EXPECTED Content  : (%s)", emailContent, item));
                }
            })
            .allMatch(emailContent::contains);
    }


    /**
     * wait until parts are costed to expected status get Job Parts
     *
     * @param jobID            - Job ID
     * @param expectedCount    - Expected parts count
     * @param connectorJobPart - ConnectorJobPart
     * @param session          - web Session
     * @return ConnectorJobParts
     */
    @SneakyThrows
    public List<ConnectorJobPart> getWorkflowJobCostedResult(String jobID, Integer expectedCount, ConnectorJobPart connectorJobPart, String session) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(WAIT_TIME);
        Boolean isCountMatched = true;
        ConnectorJobParts connectorJobParts = ThingworxUtils.getJobParts(session, jobID);
        while (getPartsCount(connectorJobParts, connectorJobPart).size() != expectedCount) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                isCountMatched = false;
            }
            TimeUnit.SECONDS.sleep(5);
            connectorJobParts = ThingworxUtils.getJobParts(session, jobID);
            driver.navigate().refresh();
        }
        if (isCountMatched) {
            return getPartsCount(connectorJobParts, connectorJobPart);
        } else {
            throw new RuntimeException("Failed Costing!!");
        }
    }


    /**
     * verify workflow job is invoked
     *
     * @param workflowID - Workflow ID
     * @param jobID      - Job ID
     * @return Boolean
     */
    @SneakyThrows
    public Boolean isWorkflowJobStarted(String workflowID, String jobID) {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(WAIT_TIME);
        List<String> jobStatusList = Arrays.asList(new String[] {"Created", "Pending", "Failed", "Errored", "Cancelled"});
        String finalJobStatus = CicApiTestUtil.getCicAgentWorkflowJobStatus(workflowID, jobID).getStatus();
        while (!jobStatusList.stream().anyMatch(finalJobStatus::contains)) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                return false;
            }
            TimeUnit.SECONDS.sleep(10);
            finalJobStatus = CicApiTestUtil.getCicAgentWorkflowJobStatus(workflowID, jobID).getStatus();
            log.debug(String.format("Job ID  >>%s<< ::: Job Status  >>%s<<", jobID, finalJobStatus));
        }
        return true;
    }

    /**
     * get the parts count by part status
     *
     * @param connectorJobParts - ConnectorJobParts
     * @param expectedJobPart   - ConnectorJobPart
     * @return Integer
     */
    public List<ConnectorJobPart> getPartsCount(ConnectorJobParts connectorJobParts, ConnectorJobPart expectedJobPart) {
        return connectorJobParts.rows.stream()
            .filter(jobPart -> jobPart.getStatus().equals(expectedJobPart.getStatus()) && jobPart.getPartType().equals(expectedJobPart.getPartType()))
            .collect(Collectors.toList());
    }

    /**
     * verify parts
     *
     * @param connectorJobParts
     * @param partList          - list of parts
     * @return Boolean
     */
    public Boolean verifyParts(List<ConnectorJobPart> connectorJobParts, List<String> partList) {
        return connectorJobParts.stream()
            .map(part -> part.getPartNumber())
            .anyMatch(part ->
                partList.stream()
                    .peek(partData -> {
                        if (!part.equals(partData)) {
                            log.debug(String.format("ACTUAL Part content : (%s) <=> EXPECTED PART NAME : (%s)", part, partData));
                        }
                    })
                    .anyMatch(partData -> part.equals(partData)
                    )
            );
    }
}