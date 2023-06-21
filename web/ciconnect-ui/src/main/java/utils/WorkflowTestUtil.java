package utils;

import com.apriori.pages.home.CIConnectHome;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

import java.util.Objects;

@Slf4j
public class WorkflowTestUtil {
    private static AgentWorkflow agentWorkflowResponse;
    private static ResponseWrapper<String> workflowResponse;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;

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
        if (agentWorkflowJobResults == null) {
            throw new RuntimeException(CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
                agentWorkflowJobRunResponse.getJobId(),
                null,
                HttpStatus.SC_OK).toString());
        }
        return agentWorkflowJobResults;
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
}
