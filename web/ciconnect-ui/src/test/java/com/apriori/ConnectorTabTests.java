package com.apriori;

import com.apriori.pages.connectors.ConnectorsPage;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import utils.WorkflowTestUtil;

public class ConnectorTabTests extends WorkflowTestUtil {
    private UserCredentials currentUser = UserUtil.getUser();

    @Test
    @TestRail(id = {3955})
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
