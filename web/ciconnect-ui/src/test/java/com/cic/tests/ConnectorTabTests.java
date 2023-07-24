package com.cic.tests;

import com.apriori.pages.connectors.ConnectorsPage;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.WorkflowTestUtil;

public class ConnectorTabTests extends WorkflowTestUtil {
    private UserCredentials currentUser = UserUtil.getUser();

    @Test
    @TestRail(testCaseId = {"3955"})
    @Description("Test Connectors List Tab")
    public void testConnectorsTabButtons() {
        SoftAssertions softAssertions = new SoftAssertions();
        ConnectorsPage connectorsPage = new CicLoginPage(driver)
            .login(currentUser)
                .clickConnectorsMenu();

        softAssertions.assertThat(connectorsPage.getConnectorText()).isEqualTo("Connectors");
        softAssertions.assertThat(connectorsPage.getNewConnectorBtn().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(connectorsPage.getDeleteConnectorBtn().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(connectorsPage.getRefreshConnectorStatusBtn().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(connectorsPage.getEditConnectorBtn().isDisplayed()).isEqualTo(true);

        connectorsPage.selectConnector(PropertiesContext.get("${customer}.ci-connect.${${customer}.ci-connect.agent_type}.connector"));
        softAssertions.assertThat(connectorsPage.getEditConnectorBtn().isEnabled()).isEqualTo(true);
        softAssertions.assertAll();
    }
}
