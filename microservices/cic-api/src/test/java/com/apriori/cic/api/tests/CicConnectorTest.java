package com.apriori.cic.api.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.apriori.cic.api.enums.CICReportType;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.ConnectorRequest;
import com.apriori.cic.api.models.request.WorkflowJob;
import com.apriori.cic.api.models.response.AgentConnectionInfo;
import com.apriori.cic.api.models.response.AgentConnectionOptions;
import com.apriori.cic.api.models.response.AgentWorkflowReportTemplates;
import com.apriori.cic.api.models.response.ConnectorInfo;
import com.apriori.cic.api.models.response.ConnectorJobParts;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.CicLoginUtil;
import com.apriori.cic.api.utils.CicUtil;
import com.apriori.cic.api.utils.ThingworxUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CicConnectorTest extends CicUtil {

    private UserCredentials currentUser = UserUtil.getUser();
    private static String loginSession;
    private static ConnectorInfo connectorInfo;
    private static SoftAssertions softAssertions;
    private static ConnectorRequest connectorRequestDataBuilder;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(currentUser).getWebSession();
        //connectorRequestDataBuilder = CicApiTestUtil.getConnectorBaseData();
    }

    @Test
    @Description("Create Connector")
    public void testAgentCreateConnector() {
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateConnector(connectorRequestDataBuilder, loginSession);
        assertThat(responseWrapper.getBody(), is(containsString("CreateAgent")));
        assertThat(responseWrapper.getBody(), is(containsString(">true<")));
        connectorInfo = CicApiTestUtil.getMatchedConnector(connectorRequestDataBuilder.getDisplayName(), loginSession);

        AgentConnectionInfo agentConnectionInfo = CicApiTestUtil.getAgentConnectorOptions(connectorInfo.getName(), loginSession);

        AgentConnectionOptions agentConnectionOptions = AgentConnectionOptions.builder()
            .agentName(connectorInfo.getName())
            .appKey(agentConnectionInfo.getAppKey())
            .wssUrl(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "url=", "#"))
            .build();
        softAssertions.assertThat(agentConnectionOptions.getAgentName()).isNotNull();
    }

    @Test
    @TestRail(id = 28435)
    @Description("Get report template names")
    public void testGetReportTemplates() {
        WorkflowJob jobPartsRequest = WorkflowJob.builder().build();
        jobPartsRequest.setJobUuid("63816f05-0acf-4908-958f-a54cb016b479");
        ConnectorJobParts connectorJobParts = ThingworxUtils.getJobParts(loginSession, "63816f05-0acf-4908-958f-a54cb016b479");
    }

    @Test
    @TestRail(id = 28435)
    @Description("Get connector Job Parts")
    public void testGetJobParts() {
        AgentWorkflowReportTemplates reportTemplates = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, loginSession);
        softAssertions.assertThat(CicApiTestUtil.getAgentReportTemplate(reportTemplates, ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY)).isNotNull();
    }

}
