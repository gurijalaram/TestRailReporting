package com.apriori.cic.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.apriori.enums.ReportsEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.ConnectorRequest;
import entity.response.AgentConnectionInfo;
import entity.response.AgentConnectionOptions;
import entity.response.AgentWorkflowReportTemplates;
import entity.response.ConnectorInfo;
import enums.CICAgentType;
import enums.CICReportType;
import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;

public class CicConnectorTest extends TestBase {

    private static String loginSession;
    private static ConnectorInfo connectorInfo;
    private static SoftAssertions softAssertions;
    private static ConnectorRequest connectorRequestDataBuilder;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = CicApiTestUtil.getLoginSession(UserUtil.getUser(), driver);
        connectorRequestDataBuilder = CicApiTestUtil.getConnectorBaseData(CICAgentType.WINDCHILL);
    }

    @Test
    @Description("Create Connector")
    public void testAgentCreateConnector() {
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateConnector(connectorRequestDataBuilder, loginSession);
        assertThat(responseWrapper.getBody(), is(containsString("CreateAgent")));
        assertThat(responseWrapper.getBody(), is(containsString(">true<")));
        connectorInfo = CicApiTestUtil.getMatchedConnector(connectorRequestDataBuilder.getDisplayName(), loginSession);

        AgentConnectionInfo agentConnectionInfo = CicApiTestUtil.getAgentConnectionOptions(connectorInfo.getName(), loginSession);

        AgentConnectionOptions agentConnectionOptions = AgentConnectionOptions.builder()
            .agentName(connectorInfo.getName())
            .appKey(agentConnectionInfo.getAppKey())
            .wssUrl(StringUtils.substringBetween(agentConnectionInfo.getConnectionInfo(), "url=", "\n\n#"))
            .build();
        softAssertions.assertThat(agentConnectionOptions.getAgentName()).isNotNull();
    }

    @Test
    @Description("Get report template names")
    public void testGetReportTemplates() {
        AgentWorkflowReportTemplates reportTemplates = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, loginSession);

        softAssertions.assertThat(CicApiTestUtil.getAgentReportTemplate(reportTemplates, ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY)).isNotNull();
    }


    @AfterClass
    public static void cleanup() {
    }
}
