package com.apriori.cic.ui.utils;

import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.cic.ui.pageobjects.home.CIConnectHome;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.shared.util.PDFDocument;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.models.response.EmailMessageAttachments;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CicGuiTestUtil extends WorkflowTestUtil {

    private static final int WAIT_TIME = 30;
    protected CIConnectHome ciConnectHome;

    /**
     * login CIC Login
     *
     * @return current class object
     */
    public CicGuiTestUtil cicGuiLogin() {
        this.ciConnectHome = new CicLoginPage(driver).login(currentUser);
        return this;
    }

    /**
     * Create workflow
     *
     * @return current class object
     */
    public CicGuiTestUtil createWorkflow() {
        this.workflowRequestDataBuilder.setCustomer(CicApiTestUtil.getCustomerName());
        this.workflowRequestDataBuilder.setPlmSystem(CicApiTestUtil.getAgent(this.ciConnectHome.getSession()));
        workflowResponse = CicApiTestUtil.createWorkflow(this.workflowRequestDataBuilder, this.ciConnectHome.getSession());
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
    public CicGuiTestUtil getWorkflow() {
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
    public CicGuiTestUtil invokeWorkflow() {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        if (agentWorkflowJobRunResponse == null) {
            throw new RuntimeException("FAILED TO INVOKE WORKFLOW!!!");
        }
        return this;
    }

    @SneakyThrows
    public CicGuiTestUtil trackWorkflow() {
        LocalTime expectedFileArrivalTime = LocalTime.now().plusMinutes(WAIT_TIME);
        List<String> jobStatusList = Arrays.asList(new String[] {"Finished", "Failed", "Errored", "Cancelled"});
        String finalJobStatus;
        Boolean isJobFinished = true;
        finalJobStatus = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId()).getStatus();
        while (!jobStatusList.stream().anyMatch(finalJobStatus::contains)) {
            if (LocalTime.now().isAfter(expectedFileArrivalTime)) {
                isJobFinished = false;
            }
            TimeUnit.SECONDS.sleep(WAIT_TIME);
            finalJobStatus = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId()).getStatus();
            ciConnectHome.refreshBrowser();
            log.debug(String.format("Job ID  >>%s<< ::: Job Status  >>%s<<", agentWorkflowResponse.getId(), finalJobStatus));
        }
        if (!isJobFinished) {
            throw new RuntimeException("FAILED TO FINISH WORKFLOW TO A TERMINAL STATE!!!");
        }

        return this;
    }

    /**
     * delete workflow
     *
     * @return current class object
     */
    public CicGuiTestUtil deleteWorkflow() {
        if (this.workflowRequestDataBuilder != null) {
            CicApiTestUtil.deleteWorkFlow(this.ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(this.workflowRequestDataBuilder.getName()));
        }
        return this;
    }

    /**
     * Create workflow of Query Definition type 'QUERY', process and get job result
     *
     * @return AgentWorkflowJobResults
     */
    @Override
    public AgentWorkflowJobResults createQueryWorkflowAndGetJobResult() {
        return this.cicGuiLogin()
            .createWorkflow()
            .getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();
    }


}
