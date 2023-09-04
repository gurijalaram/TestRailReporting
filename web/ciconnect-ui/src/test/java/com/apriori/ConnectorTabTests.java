package com.apriori;

import com.apriori.cic.enums.PlmTypeAttributes;
import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.enums.ConnectorType;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.connectors.ConnectorDetails;
import com.apriori.pageobjects.connectors.ConnectorMappings;
import com.apriori.pageobjects.connectors.ConnectorsPage;
import com.apriori.pageobjects.connectors.StandardFields;
import com.apriori.pageobjects.home.CIConnectHome;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConnectorTabTests extends TestBaseUI {
    private UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions;

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {3955})
    @Description("Test Connectors List Tab")
    public void testConnectorsTabButtons() {
        ConnectorsPage connectorsPage = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu();

        softAssertions.assertThat(connectorsPage.getConnectorText()).isEqualTo("Connectors");
        softAssertions.assertThat(connectorsPage.getNewConnectorBtn().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(connectorsPage.getDeleteConnectorBtn().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(connectorsPage.getRefreshConnectorStatusBtn().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(connectorsPage.getEditConnectorBtn().isDisplayed()).isEqualTo(true);

        connectorsPage.selectConnector(CicApiTestUtil.getAgentPortData().getConnector());
        softAssertions.assertThat(connectorsPage.getEditConnectorBtn().isEnabled()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {3960, 4085, 3998})
    @Description("New connector modal navigation, New Connector Details, Test 3 Required Fields are filled in correctly depending on which PLM system we are connected to ")
    public void testConnectorDetails() {
        String connectorName =  GenerateStringUtil.saltString("--CON");
        ConnectorDetails connectorDetails = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn();

        softAssertions.assertThat(connectorDetails.isLabelElementDisplayed("Name")).isTrue();
        softAssertions.assertThat(connectorDetails.isLabelElementDisplayed("Type")).isTrue();
        softAssertions.assertThat(connectorDetails.getConnectionInfoElement().getAttribute("readonly")).isEqualTo("true");

        ConnectorMappings connectorMappings = connectorDetails.enterConnectorName(connectorName)
            .selectType(ConnectorType.WINDCHILL)
            .clickNextBtn();

        StandardFields standardFields = connectorMappings.selectStandardFieldsTab();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_ID, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_REVISION, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_NUMBER, 1).isEnabled()).isFalse();

        standardFields.clickSaveBtn();
        softAssertions.assertThat(standardFields.getStatusMessage().contains(PlmTypeAttributes.PLM_REVISION.getCicGuiField())).isTrue();
        softAssertions.assertThat(standardFields.getStatusMessage().contains(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField())).isTrue();
        standardFields = standardFields.closeMessageAlert(StandardFields.class);

        standardFields.enterPlmField(PlmTypeAttributes.PLM_REVISION).clickSaveBtn();
        softAssertions.assertThat(standardFields.getStatusMessage().contains(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField())).isTrue();
        standardFields = standardFields.closeMessageAlert(StandardFields.class);

        CIConnectHome ciConnectHome = standardFields.enterPlmField(PlmTypeAttributes.PLM_REVISION)
            .enterPlmField(PlmTypeAttributes.PLM_PART_NUMBER)
            .clickSaveBtn();
        softAssertions.assertThat(ciConnectHome.getStatusMessage()).isEqualTo("Connector created!");
        standardFields = standardFields.closeMessageAlert(StandardFields.class);

        ConnectorsPage connectorsPage = ciConnectHome.clickConnectorsMenu()
            .selectConnector(connectorName)
            .clickDeleteBtn()
            .clickConfirmAlertDelete();
        softAssertions.assertThat(connectorsPage.isConnectorExist(connectorName)).isFalse();
    }

    @AfterEach
    public void cleanUp() {
        softAssertions.assertAll();
    }
}
