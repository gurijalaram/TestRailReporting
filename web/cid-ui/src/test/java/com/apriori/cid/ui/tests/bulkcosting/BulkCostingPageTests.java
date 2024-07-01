package com.apriori.cid.ui.tests.bulkcosting;

import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.InstallationUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.projects.BulkCostingPage;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

// TODO: 01/07/2024 cn - all these tests need to be updated and fixed
public class BulkCostingPageTests extends TestBaseUI {
    private final String saltName = new GenerateStringUtil().saltString("name");
    private CidAppLoginPage loginPage;
    private BulkCostingPage bulkCostingPage;
    private String worksheetIdentity;
    private BcmUtil bcmUtil;
    private UserCredentials userCredentials = UserUtil.getUser();

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        bcmUtil = new BcmUtil(requestEntityUtil);
    }

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            BcmUtil bcmUtil = new BcmUtil();
            bcmUtil.deleteWorksheetWithEmail(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT, UserCredentials.init(userCredentials.getEmail(), null));
            worksheetIdentity = null;
        }
    }

    @Test
    @TestRail(id = {29187, 29874, 29942})
    @Description("bulk costing page visibility, adding and delete worksheet")
    public void bulkCostingAddAndDeleteWorksheet() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkAnalysis();

        soft.assertThat(bulkCostingPage.isListOfWorksheetsPresent()).isTrue();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);

        bulkCostingPage.selectAndDeleteSpecificBulkAnalysis(worksheetResponse.getName());
        worksheetIdentity = null;
        soft.assertThat(bulkCostingPage.isWorksheetNotPresent(worksheetResponse.getName())).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30730, 29964, 31068})
    @Description("create inputRow for the worksheet and go to evaluate page")
    public void testCreateInputAndGoToEvaluatePageRow() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);

        String inputRowName1 = createInputRow(userCredentials, worksheetResponse, 5);
        String inputRowName2 = createInputRow(userCredentials, worksheetResponse, 6);
        bulkCostingPage.enterSpecificBulkAnalysis(worksheetResponse.getName());

        soft.assertThat(bulkCostingPage.isInputRowDisplayed(inputRowName1)).isTrue();
        soft.assertThat(bulkCostingPage.isInputRowDisplayed(inputRowName2)).isTrue();

        EvaluatePage evaluatePage = bulkCostingPage.selectComponentByRow(1);
        soft.assertThat(evaluatePage.isDesignGuidanceButtonDisplayed()).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30679, 30680, 30681, 30682, 30684})
    @Description("delete input row for the worksheet")
    public void testDeleteInputRow() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);

        String inputRowName = createInputRow(userCredentials, worksheetResponse, 5);
        bulkCostingPage.enterSpecificBulkAnalysis(worksheetResponse.getName());

        soft.assertThat(bulkCostingPage.getRemoveButtonState("Cannot perform a remove action"))
            .contains(List.of("Cannot perform a remove action with no scenarios selected"));

        bulkCostingPage.selectFirstPartInWorkSheet();
        soft.assertThat(bulkCostingPage.getRemoveButtonState("Remove"))
            .isEqualTo("Remove");
        soft.assertThat(bulkCostingPage.clickOnRemoveButtonAngGetConfirmationText())
            .contains(Arrays.asList("You are attempting to remove", "from the bulk analysis. This action cannot be undone."));

        bulkCostingPage.clickOnRemoveScenarioButtonOnConfirmationScreen();
        soft.assertThat(bulkCostingPage.isScenarioPresentOnPage(inputRowName)).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30675, 30676, 30674})
    @Description("update inputs")
    public void testUpdateInputs() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);
        createInputRow(userCredentials, worksheetResponse, 5);
        bulkCostingPage.enterSpecificBulkAnalysis(worksheetResponse.getName());

        soft.assertThat(bulkCostingPage.getSetInputButtonState("Cannot set inputs with no scenarios selected."))
            .isEqualTo("Cannot set inputs with no scenarios selected.");

        bulkCostingPage.selectFirstPartInWorkSheet();

        soft.assertThat(bulkCostingPage.getSetInputButtonState("Set Inputs"))
            .isEqualTo("Set Inputs");

        bulkCostingPage.clickSetInputsButton();
        soft.assertThat(bulkCostingPage.isScenarioPresentOnPage()).isTrue();
        bulkCostingPage.selectDropdownProcessGroup("2-Model Machining")
            .selectDropdownDigitalFactory("AGCO Assumption")
            .typeIntoAnnualVolume("100")
            .typeIntoYears("5")
            .clickOnSaveButtonOnSetInputs()
            .clickOnCloseButtonOnSetInputs();

        soft.assertThat(bulkCostingPage.isElementDisplayedOnThePage("2-Model Machining")).isTrue();
        soft.assertThat(bulkCostingPage.isElementDisplayedOnThePage("AGCO Assumption")).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29876, 29875, 29877, 29878, 29924})
    @Description("edit Bulk Analysis name and later on search on it")
    public void editBulkAnalysisNameAndSearchOnIt() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);
        createInputRow(userCredentials, worksheetResponse, 5);
        soft.assertThat(bulkCostingPage.getInfoButtonState("Cannot show worksheet info with no worksheet selected."))
            .isEqualTo("Cannot show worksheet info with no worksheet selected.");

        bulkCostingPage.selectBulkAnalysis(worksheetResponse.getName());
        soft.assertThat(bulkCostingPage.getInfoButtonState("Worksheet Info")).isEqualTo("Worksheet Info");

        bulkCostingPage.clickOnTheInfoButton();
        soft.assertThat(bulkCostingPage.isBulkAnalysisInfoWindowIsDisplayed()).isTrue();
        String worksheetName = worksheetResponse.getName().concat("_updated");
        bulkCostingPage.changeTheNaneOfBulkAnalysisName(worksheetName)
            .clickOnSaveButtonOnBulkAnalysisInfo();
        soft.assertThat(bulkCostingPage.isWorksheetPresent(worksheetName)).isTrue();

        bulkCostingPage.typeIntoSearchWorkSheetInput(worksheetName)
            .clickOnSubmitOnSearchBulkAnalysis();

        soft.assertThat(bulkCostingPage.checkExpectedNumbersOfRows(1)).isTrue();
        soft.assertThat(bulkCostingPage.getTextFromTheFirstRow()).contains(List.of(worksheetName));
        soft.assertAll();
    }

    private String createInputRow(UserCredentials userCredentials, WorkSheetResponse worksheetResponse, int itemNumber) {
        CssComponent cssComponent = new CssComponent();
        BcmUtil bcmUtil = new BcmUtil();
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(userCredentials, "PART")
                .getResponseEntity().getItems().get(itemNumber);

        bcmUtil.createWorkSheetInputRowWithEmail(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(), worksheetResponse.getIdentity(), userCredentials);

        return scenarioItem.getComponentDisplayName();
    }

    private void setBulkCostingFlag(boolean bulkCostingValue) {
        RequestEntityUtil requestEntityUtil = TestHelper.initCustomUser(userCredentials);
        InstallationUtil installationUtil = new InstallationUtil(requestEntityUtil);
        CdsTestUtil cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        String customerIdentity = SharedCustomerUtil.getCustomerData().getIdentity();

        ResponseWrapper<Deployments> deployments = cdsTestUtil.getCommonRequest(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID,
            Deployments.class,
            HttpStatus.SC_OK,
            customerIdentity
        );

        Deployment deployment =
            deployments.getResponseEntity().getItems().stream().filter(item -> item.getDescription().equals("Production deployment"))
                .findFirst().orElse(null);
        String deploymentIdentity = deployment.getIdentity();

        String installationIdentity =
            deployment.getInstallations().stream().filter(item -> !(item.getUrl().equals("NA")))
                .findFirst().orElse(null).getIdentity();

        installationUtil.updateFeature(bulkCostingValue, customerIdentity, deploymentIdentity, installationIdentity);
    }
}


