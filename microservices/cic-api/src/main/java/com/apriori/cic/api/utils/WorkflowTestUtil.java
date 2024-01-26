package com.apriori.cic.api.utils;

import com.apriori.cic.api.enums.CICAPIEnum;
import com.apriori.cic.api.models.request.WorkflowParts;
import com.apriori.cic.api.models.request.WorkflowRequest;
import com.apriori.cic.api.models.response.AgentWorkflow;
import com.apriori.cic.api.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.shared.util.PDFDocument;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.EmailMessageAttachments;
import com.apriori.shared.util.testconfig.TestBaseUI;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WorkflowTestUtil extends TestBaseUI {
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
    public WorkflowTestUtil create(WorkflowRequest workflowRequestData, String sessionID) {
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
        return create(workflowRequestData, sessionID)
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
}