package com.apriori;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.apriori.cic.enums.CICReportType;
import com.apriori.cic.enums.ReportsEnum;
import com.apriori.cic.models.request.ConnectorRequest;
import com.apriori.cic.models.response.AgentConnectionInfo;
import com.apriori.cic.models.response.AgentConnectionOptions;
import com.apriori.cic.models.response.AgentWorkflowReportTemplates;
import com.apriori.cic.models.response.ConnectorInfo;
import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.cic.utils.CicLoginUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;

import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CicConnectorTest extends TestBaseUI {

    private UserCredentials currentUser = UserUtil.getUser();
    private static String loginSession;
    private static ConnectorInfo connectorInfo;
    private static SoftAssertions softAssertions;
    private static ConnectorRequest connectorRequestDataBuilder;

    @AfterAll
    public static void cleanup() {
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
    @Description("Get report template names")
    public void testGetReportTemplates() {
        AgentWorkflowReportTemplates reportTemplates = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, loginSession);

        softAssertions.assertThat(CicApiTestUtil.getAgentReportTemplate(reportTemplates, ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY)).isNotNull();
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession =  new CicLoginUtil(driver).login(currentUser).navigateToUserMenu().getWebSession();
        connectorRequestDataBuilder = CicApiTestUtil.getConnectorBaseData();
    }
}
