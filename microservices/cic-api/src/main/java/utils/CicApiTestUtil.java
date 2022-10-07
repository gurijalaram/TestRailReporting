package utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.pages.login.LoginPage;
import com.apriori.pages.users.UsersPage;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import entity.request.workflow.JobDefinition;
import entity.request.workflow.Workflow;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobRun;
import enums.CICAPIEnum;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CicApiTestUtil extends TestUtil {

    /**
     * Submit CIC Agent Api request and return response
     *
     * @param endPoint - CICAPIEnum
     * @param klass    - response class name
     * @param <T>      - class object
     * @return response of klass object
     */
    public static <T> ResponseWrapper<T> submitRequest(CICAPIEnum endPoint, Class<T> klass) {
        RequestEntity requestEntity = RequestEntityUtil.init(endPoint, klass);
        requestEntity.headers(setUpHeader());
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit request to get Agent workflow
     *
     * @param workFlowID
     * @return response of AgentWorkflow object
     */
    public static ResponseWrapper<AgentWorkflow> getCicAgentWorkflow(String workFlowID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW, AgentWorkflow.class).inlineVariables(workFlowID);
        requestEntity.headers(setUpHeader());
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit request to get CIC agent workflow jobs
     *
     * @param workFlowID
     * @return response
     */
    public static ResponseWrapper<String> getCicAgentWorkflowJobs(String workFlowID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOBS, null).inlineVariables(workFlowID);
        requestEntity.headers(setUpHeader());
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit request to get CIC agent workflow job
     *
     * @param workFlowID
     * @param jobID
     * @return response of AgentWorkflowJob object
     */
    public static ResponseWrapper<AgentWorkflowJob> getCicAgentWorkflowJob(String workFlowID, String jobID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB, AgentWorkflowJob.class)
            .inlineVariables(workFlowID, jobID);
        requestEntity.headers(setUpHeader());
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Submit request to run the CIC agent workflow
     *
     * @param workflowId
     * @return response of AgentWorkflowJob object
     */
    public static ResponseWrapper<AgentWorkflowJobRun> runCicAgentWorkflow(String workflowId) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_RUN, AgentWorkflowJobRun.class)
            .inlineVariables(workflowId);
        requestEntity.headers(setUpHeader());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Submit CIC thingworx API to create a new workflow
     *
     * @param session  - Login to CI-Connect GUI to get JSession
     * @param workflow - deserialized workflowdata
     * @return response of created work flow string
     */
    public static ResponseWrapper<String> CreateWorkflow(String session, Workflow workflow) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        header.put("cookie", session.replace("[", "").replace("]", ""));
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_CREATE_WORKFLOW, null)
            .headers(header)
            .body(workflow);
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
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        header.put("cookie", session.replace("[", "").replace("]", ""));

        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_UI_DELETE_WORKFLOW, null)
            .headers(header)
            .body(jobDefinition);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Submit request to get all CIC agent workflows
     *
     * @return response
     */
    public static ResponseWrapper<String> cancelWorkflow(String workFlowID, String workflowJobID) {
        RequestEntity requestEntity = RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_CANCEL, null)
            .inlineVariables(workFlowID, workflowJobID);
        requestEntity.headers(setUpHeader());
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Login to CI-connect GUI to get JSessionID
     *
     * @param currentUser - user credentials to login to ci-connect gui
     * @param webDriver   - WebDriver
     * @return JSessionID
     */
    public static String getLoginSession(UserCredentials currentUser, WebDriver webDriver) {
        UsersPage usersPage = new LoginPage(webDriver)
            .login(currentUser)
            .clickUsersMenu();
        return String.valueOf(webDriver.manage().getCookies()).replace("[", "").replace("]", "");
    }

    /**
     * setup header information for CIC Agent API Authorization
     *
     * @return Map
     */
    public static Map<String, String> setUpHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Authorization", PropertiesContext.get("${env}.ci-connect.agent_api_authorization_key"));
        return header;
    }

    /**
     * Submit request to get list of workflows and then get the matched workflow
     *
     * @param workFlowName - WorkflowName
     * @return AgentWorkflow java POJO contains matched workflow information.
     */
    public static AgentWorkflow getMatchedWorkflowId(String workFlowName) {
        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        return Arrays.stream(agentWorkflows)
            .filter(wf -> wf.getName().equals(workFlowName))
            .findFirst()
            .get();
    }
}