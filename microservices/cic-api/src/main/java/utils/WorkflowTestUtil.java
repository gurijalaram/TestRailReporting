package utils;

import com.apriori.utils.http.utils.ResponseWrapper;

import entity.request.WorkflowParts;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;


@Slf4j
public class WorkflowTestUtil {
    private AgentWorkflow agentWorkflowResponse;
    private ResponseWrapper<String> workflowResponse;
    private AgentWorkflowJobRun agentWorkflowJobRunResponse;

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
            log.info(String.format("WORKFLOW CREATED SUCCESSFULLY (%s)", workflowRequestData.getName()));
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
    public WorkflowTestUtil invoke() {
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
            throw new RuntimeException(agentWorkflowJobResults.get(0).getErrorMessage());
        }
        return agentWorkflowJobResults;
    }

    /**
     * get workflow job part results
     *
     * @return Current class object
     */
    public AgentWorkflowJobPartsResult getJobPartResult(String partId) {
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            partId,
            AgentWorkflowJobPartsResult.class,
            HttpStatus.SC_OK);

        if (agentWorkflowJobPartsResult.getInput() == null) {
            throw new RuntimeException(agentWorkflowJobPartsResult.getErrorMessage());
        }
        return agentWorkflowJobPartsResult;
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
            .invoke()
            .track()
            .getJobResult();
    }

    /**
     * Create workflow and get job part results after processing the workflow
     *
     * @param workflowRequestData - Data to create the workflow
     * @param workflowParts       - Number of parts added to workflow
     * @param sessionID           - JSESSIONID
     * @return AgentWorkflowJobPartsResult
     */
    public AgentWorkflowJobPartsResult createWorkflowAndGetJobPartResult(WorkflowRequest workflowRequestData, WorkflowParts workflowParts, String sessionID) {
        return create(workflowRequestData, sessionID)
            .getWorkflowId(workflowRequestData.getName())
            .invokeRestWorkflow(workflowParts)
            .track()
            .getJobPartResult(workflowParts.getParts().get(0).getId());
    }

    public AgentWorkflow getAgentWorkflowResponse() {
        return agentWorkflowResponse;
    }

    public AgentWorkflowJobRun getAgentWorkflowJobRunResponse() {
        return agentWorkflowJobRunResponse;
    }
}
