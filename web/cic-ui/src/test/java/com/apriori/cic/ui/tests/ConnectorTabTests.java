package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.CICAgentType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.ui.enums.ConnectorListHeaders;
import com.apriori.cic.ui.enums.ConnectorType;
import com.apriori.cic.ui.enums.FieldDataType;
import com.apriori.cic.ui.enums.SortedOrderType;
import com.apriori.cic.ui.enums.UsageRule;
import com.apriori.cic.ui.pageobjects.connectors.AdditionalPlmFields;
import com.apriori.cic.ui.pageobjects.connectors.ConnectorDetails;
import com.apriori.cic.ui.pageobjects.connectors.ConnectorMappings;
import com.apriori.cic.ui.pageobjects.connectors.ConnectorsPage;
import com.apriori.cic.ui.pageobjects.connectors.StandardFields;
import com.apriori.cic.ui.pageobjects.home.CIConnectHome;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ConnectorTabTests extends TestBaseUI {
    private UserCredentials currentUser = UserUtil.getUser();
    private SoftAssertions softAssertions;

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = {3955, 4871})
    @Description("Test Connectors List Tab" +
        "Test that a Connector with any associated WF's cannot be deleted")
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
        softAssertions.assertThat(connectorsPage.getDeleteConnectorBtn().isEnabled()).isFalse();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {3960, 3998})
    @Description("New connector modal navigation, New Connector Details, " +
        "Test 3 Required Fields are filled in correctly depending on which PLM system we are connected to " +
        "Check required fields highlighted in red/display message prompting input")
    public void testNewConnectorDetails() {
        String connectorName = GenerateStringUtil.saltString("--CON");
        ConnectorDetails connectorDetails = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn();

        softAssertions.assertThat(connectorDetails.getNameErrorMsg()).isEqualTo("Name is required.");
        softAssertions.assertThat(connectorDetails.getTypeErrorMsg()).isEqualTo("Type is required.");
        softAssertions.assertThat(connectorDetails.getConnectionInfoElement().getAttribute("readonly")).isEqualTo("true");

        ConnectorMappings connectorMappings = connectorDetails.enterConnectorName(connectorName)
            .selectType(ConnectorType.WINDCHILL)
            .clickNextBtn();

        connectorMappings.selectStandardFieldsTab()
            .enterPlmField(PlmTypeAttributes.PLM_REVISION)
            .enterPlmField(PlmTypeAttributes.PLM_PART_NUMBER);

        CIConnectHome ciConnectHome = connectorMappings.selectAdditionalPlmFieldsTab()
            .addRow(PlmTypeAttributes.PLM_DESCRIPTION, UsageRule.READ_FROM, FieldDataType.DT_STRING)
            .clickSaveBtn();

        softAssertions.assertThat(ciConnectHome.getStatusMessage()).isEqualTo("Connector created!");
        ciConnectHome.closeMessageAlert();
        ConnectorsPage connectorsPage = ciConnectHome.clickConnectorsMenu().selectConnector(connectorName).clickDeleteBtn().clickConfirmAlertDelete();
        softAssertions.assertThat(connectorsPage.isConnectorExist(connectorName)).isFalse();
    }

    @Test
    @TestRail(id = {4085, 4333})
    @Description("Test 3 Required Fields are filled in correctly depending on which PLM system we are connected to " +
        "Check required fields highlighted in red/display message prompting input")
    public void testConnDetailsRequiredFields() {
        String connectorName = GenerateStringUtil.saltString("--CON");
        ConnectorMappings connectorMappings = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn()
            .enterConnectorName(connectorName)
            .selectType(ConnectorType.WINDCHILL)
            .clickNextBtn();

        StandardFields standardFields = connectorMappings.selectStandardFieldsTab();

        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_ID, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_REVISION, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_NUMBER, 1).isEnabled()).isFalse();

        CIConnectHome ciConnectHome = standardFields.clickSaveBtn();
        softAssertions.assertThat(ciConnectHome.getStatusMessage().contains(PlmTypeAttributes.PLM_REVISION.getCicGuiField())).isTrue();
        softAssertions.assertThat(ciConnectHome.getStatusMessage().contains(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField())).isTrue();
        ciConnectHome.closeMessageAlert();

        standardFields.enterPlmField(PlmTypeAttributes.PLM_REVISION).clickSaveBtn();
        softAssertions.assertThat(standardFields.getStatusMessage().contains(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField())).isTrue();
        standardFields.closeMessageAlert();

        AdditionalPlmFields additionalPlmFields = connectorMappings.selectAdditionalPlmFieldsTab().clickAddRowBtn();
        softAssertions.assertThat(additionalPlmFields.getAdditionalPlmFieldsRows().size()).isEqualTo(2);
    }

    @Test
    @TestRail(id = {4168, 3999})
    @Description("Verify Connector may not be created with an invalid name by first entering a valid name and then changing input" +
        "Test input field validation for New Connector modal")
    public void testConnectorDetailsValidation() {
        String connectorName = GenerateStringUtil.saltString("--CON");
        ConnectorDetails connectorDetails = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn();

        connectorDetails = connectorDetails.enterConnectorName(connectorName).selectType(ConnectorType.WINDCHILL);
        softAssertions.assertThat(connectorDetails.isNextBtnEnabled(true)).isTrue();

        connectorDetails = connectorDetails.enterConnectorName("");
        softAssertions.assertThat(connectorDetails.isNextBtnEnabled(false)).isTrue();

        connectorDetails = connectorDetails.enterConnectorName("*");
        softAssertions.assertThat(connectorDetails.getNameErrorMsg()).isEqualTo("Name should only contain spaces and the following characters: a-zA-Z0-9-_");

        connectorDetails = connectorDetails.enterConnectorName(CicApiTestUtil.getAgentPortData().getConnector());
        softAssertions.assertThat(connectorDetails.getNameErrorMsg()).isEqualTo(String.format("A Connector with the name %s already exists.", CicApiTestUtil.getAgentPortData().getConnector()));

        connectorDetails = connectorDetails.enterConnectorName(new GenerateStringUtil().getRandomStringSpecLength(70));
        softAssertions.assertThat(connectorDetails.getNameErrorMsg()).isEqualTo("Name must be less than or equal to 64 characters.");

        connectorDetails = connectorDetails.enterConnectorName(connectorName).enterConnectorDescription(new GenerateStringUtil().getRandomStringSpecLength(260));
        softAssertions.assertThat(connectorDetails.getDescriptionErrorMsg()).isEqualTo("Description must be less than or equal to 255 characters.");

        ConnectorMappings connectorMappings = connectorDetails.enterConnectorName(connectorName)
            .enterConnectorDescription(connectorName)
            .clickNextBtn();

        StandardFields standardFields = connectorMappings.selectStandardFieldsTab()
            .enterPlmField(PlmTypeAttributes.PLM_REVISION, new GenerateStringUtil().getRandomStringSpecLength(210));

        CIConnectHome ciConnectHome = standardFields.clickSaveBtn();
        softAssertions.assertThat(ciConnectHome.getStatusMessage().contains("PLM Field must be less than or equal to 200 characters")).isTrue();
        ciConnectHome.closeMessageAlert();
        standardFields.enterPlmField(PlmTypeAttributes.PLM_REVISION).enterPlmField(PlmTypeAttributes.PLM_PART_NUMBER);

        AdditionalPlmFields additionalPlmFields = connectorMappings.selectAdditionalPlmFieldsTab()
            .addRow(PlmTypeAttributes.PLM_PART_NUMBER, UsageRule.READ_FROM, FieldDataType.DT_STRING);

        ciConnectHome = additionalPlmFields.clickSaveBtn();
        softAssertions.assertThat(ciConnectHome.getStatusMessage().contains("The 'Part Number' CI Connect Field is already in use by this connector")).isTrue();
        ciConnectHome.closeMessageAlert();
    }

    @Test
    @TestRail(id = {4000, 4001, 4199, 4201})
    @Description("Test New Connector PLM Mappings Tab Standard Fields Sub-tab, " +
        "Test New Connector PLM Mappings Tab Additional Fields Sub-tab" +
        "Check correct data type specified for each of the Standard CI Connect Fields" +
        "Check data persistence when switching tabs")
    public void testConnectorStandardMappings() {
        String connectorName = GenerateStringUtil.saltString("--CON");
        StandardFields standardFields = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn()
            .enterConnectorName(connectorName)
            .selectType(ConnectorType.WINDCHILL)
            .clickNextBtn()
            .selectStandardFieldsTab()
            .enterPlmField(PlmTypeAttributes.PLM_REVISION)
            .enterPlmField(PlmTypeAttributes.PLM_PART_NUMBER)
            .clickAddRowBtn()
            .selectRow();

        softAssertions.assertThat(standardFields.getStandardFieldsRows().size()).isEqualTo(4);
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_DESCRIPTION).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_DIGITAL_FACTORY).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_CUSTOM_EMAIL).getFieldDataType()).isEqualTo(FieldDataType.DT_EMAIL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_PROCESS_GROUP).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_MATERIAL_NAME).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_ANNUAL_VOLUME).getFieldDataType()).isEqualTo(FieldDataType.DT_INTEGER.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_BATCH_SIZE).getFieldDataType()).isEqualTo(FieldDataType.DT_INTEGER.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_PRODUCTION_LIFE).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_CAPITAL_INVESTMENT).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_FULLY_BURDENED_COST).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_CYCLE_TIME).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_LABOR_TIME).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_FINISH_MASS).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_ROUGH_MASS).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_UTILIZATION).getFieldDataType()).isEqualTo(FieldDataType.DT_REAL.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_DFM_RISK).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_CURRENCY_CODE).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());
        softAssertions.assertThat(standardFields.selectCiConnectField(PlmTypeAttributes.PLM_SCENARIO_NAME).getFieldDataType()).isEqualTo(FieldDataType.DT_STRING.getDataType());

        standardFields = standardFields.removeRow();
        softAssertions.assertThat(standardFields.getStandardFieldsRows().size()).isEqualTo(3);

        ConnectorDetails connectorDetails = standardFields.clickPreviousBtn();
        softAssertions.assertThat(connectorDetails.getConnectorName()).isEqualTo(connectorName);
        standardFields = connectorDetails.clickNextBtn().selectStandardFieldsTab();
        softAssertions.assertThat(standardFields.getPlmFieldValue(PlmTypeAttributes.PLM_REVISION)).isEqualTo(PlmTypeAttributes.PLM_REVISION.getValue());
        softAssertions.assertThat(standardFields.getPlmFieldValue(PlmTypeAttributes.PLM_PART_NUMBER)).isEqualTo(PlmTypeAttributes.PLM_PART_NUMBER.getValue());
    }

    @Test
    @TestRail(id = {3953, 4871})
    @Description("Test Connectors List" +
        "Test that a Connector with any associated WF's cannot be deleted")
    public void testConnectorsList() {
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
        softAssertions.assertThat(connectorsPage.getDeleteConnectorBtn().isEnabled()).isFalse();
        softAssertions.assertThat(connectorsPage.isConnectorListIsSorted(ConnectorListHeaders.TYPE, SortedOrderType.ASCENDING));
        softAssertions.assertThat(connectorsPage.isConnectorListIsSorted(ConnectorListHeaders.CONNECTION_STATUS, SortedOrderType.ASCENDING));
        softAssertions.assertThat(connectorsPage.isConnectorListIsSorted(ConnectorListHeaders.NAME, SortedOrderType.ASCENDING));
    }

    @Test
    @TestRail(id = {4742})
    @Description("Test if PLM Type = File System Mandatory Fields are different")
    public void testCreateAndDeleteConnectorFileSystem() {
        String connectorName = GenerateStringUtil.saltString("--CON");
        ConnectorMappings connectorMappings = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn()
            .enterConnectorName(connectorName)
            .selectType(ConnectorType.FILE_SYSTEM)
            .clickNextBtn();

        CIConnectHome ciConnectHome  = connectorMappings.selectStandardFieldsTab()
            .enterPlmField(PlmTypeAttributes.PLM_PART_ID)
            .enterPlmField(PlmTypeAttributes.PLM_CAD_FILE_NAME)
            .enterPlmField(PlmTypeAttributes.PLM_APRIORI_PART_NUMBER)
            .clickSaveBtn();

        softAssertions.assertThat(ciConnectHome.getStatusMessage()).isEqualTo("Connector created!");
        ciConnectHome.closeMessageAlert();
        ConnectorsPage connectorsPage = ciConnectHome.clickConnectorsMenu().selectConnector(connectorName).clickDeleteBtn().clickConfirmAlertDelete();
        softAssertions.assertThat(connectorsPage.isConnectorExist(connectorName)).isFalse();
    }

    @Test
    @TestRail(id = {4743})
    @Description("Test if PLM Type is changed during Connector Creation that the UI Behaves as expected ")
    public void testConnSwitchBetweenType() {
        String connectorName = GenerateStringUtil.saltString("--CON");
        ConnectorMappings connectorMappings = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
            .clickNewBtn()
            .enterConnectorName(connectorName)
            .selectType(ConnectorType.WINDCHILL)
            .clickNextBtn();

        StandardFields standardFields = connectorMappings.selectStandardFieldsTab();

        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_ID, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_REVISION, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_NUMBER, 1).isEnabled()).isFalse();

        ConnectorDetails connectorDetails = standardFields.clickPreviousBtn();
        standardFields = connectorDetails.selectType(ConnectorType.TEAM_CENTER).clickNextBtn().selectStandardFieldsTab();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_ID, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_REVISION, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_PART_NUMBER, 1).isEnabled()).isFalse();
        softAssertions.assertThat(standardFields.getMatchedConnectFieldColumn(PlmTypeAttributes.PLM_SEQUENCE_ID, 1).isEnabled()).isFalse();
    }

    @Test
    @TestRail(id = {4003})
    @Description("Fields populated with existing connector information")
    public void testEditConnectors() {
        ConnectorDetails connectorDetails = new CicLoginPage(driver)
            .login(currentUser)
            .clickConnectorsMenu()
                .selectConnector(CicApiTestUtil.getAgentPortData().getConnector()).clickEditBtn();

        softAssertions.assertThat(connectorDetails.getConnectorName()).isEqualTo(CicApiTestUtil.getAgentPortData().getConnector());
        ConnectorsPage connectorsPage = connectorDetails.clickCancelBtn();
        connectorDetails = connectorsPage.selectConnector(CicApiTestUtil.getAgentPortData(CICAgentType.TEAM_CENTER).getConnector()).clickEditBtn();
        softAssertions.assertThat(connectorDetails.getConnectorName()).isEqualTo(CicApiTestUtil.getAgentPortData(CICAgentType.TEAM_CENTER).getConnector());
    }

    @AfterEach
    public void cleanUp() {
        softAssertions.assertAll();
    }
}
