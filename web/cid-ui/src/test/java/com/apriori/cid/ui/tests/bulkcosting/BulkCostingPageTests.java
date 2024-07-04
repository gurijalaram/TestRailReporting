package com.apriori.cid.ui.tests.bulkcosting;

import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.InstallationUtil;
import com.apriori.cid.ui.pageobjects.bulkanalysis.BulkAnalysisInfoPage;
import com.apriori.cid.ui.pageobjects.bulkanalysis.BulkAnalysisPage;
import com.apriori.cid.ui.pageobjects.bulkanalysis.SetInputsModalPage;
import com.apriori.cid.ui.pageobjects.bulkanalysis.WorksheetsExplorePage;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.DeletePage;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BulkCostingPageTests extends TestBaseUI {
    private final String saltName = GenerateStringUtil.saltString("name");
    private CidAppLoginPage loginPage;
    private BulkAnalysisPage bulkAnalysisPage;
    private EvaluatePage evaluatePage;
    private WorksheetsExplorePage worksheetsExplorePage;
    private DeletePage deletePage;
    private SetInputsModalPage setInputsModalPage;
    private BulkAnalysisInfoPage bulkAnalysisInfoPage;
    private String worksheetIdentity;
    private BcmUtil bcmUtil;
    private RequestEntityUtil requestEntityUtil;
    private SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        bcmUtil = new BcmUtil(requestEntityUtil);
    }

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            bcmUtil.deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
            worksheetIdentity = null;
        }
    }

    @Test
    @TestRail(id = {29187, 29874, 29942})
    @Description("Test bulk costing page visibility, add and delete worksheet")
    public void testAddAndDeleteWorksheet() {
        setBulkCostingFlag(true);

        loginPage = new CidAppLoginPage(driver);
        bulkAnalysisPage = loginPage
            .login(requestEntityUtil.getEmbeddedUser())
            .clickBulkAnalysis();

        softAssertions.assertThat(bulkAnalysisPage.getListOfWorksheets()).isGreaterThan(0);

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);

        bulkAnalysisPage.highlightWorksheet(worksheetResponse.getName());
        softAssertions.assertThat(bulkAnalysisPage.isWorksheetPresent(worksheetResponse.getName())).isFalse();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30730, 29964, 31068})
    @Description("Create input row for the worksheet and go to evaluate page")
    public void testCreateInputAndGoToEvaluatePage() {
        setBulkCostingFlag(true);


        loginPage = new CidAppLoginPage(driver);
        bulkAnalysisPage = loginPage
            .login(requestEntityUtil.getEmbeddedUser())
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);
        String inputRowName1 = bcmUtil.searchCreateInputRow(requestEntityUtil.getEmbeddedUser(), worksheetResponse, 5);
        String inputRowName2 = bcmUtil.searchCreateInputRow(requestEntityUtil.getEmbeddedUser(), worksheetResponse, 6);

        worksheetsExplorePage = bulkAnalysisPage.openWorksheet(worksheetResponse.getName());
        softAssertions.assertThat(worksheetsExplorePage.isInputRowDisplayed(inputRowName1)).isGreaterThan(0);
        softAssertions.assertThat(worksheetsExplorePage.isInputRowDisplayed(inputRowName2)).isGreaterThan(0);

        evaluatePage = worksheetsExplorePage.openComponentByRow(1);
        softAssertions.assertThat(evaluatePage.isDesignGuidanceButtonDisplayed()).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30679, 30680, 30681, 30682, 30684})
    @Description("Delete input row for the worksheet")
    public void testDeleteInputRow() {
        setBulkCostingFlag(true);

        loginPage = new CidAppLoginPage(driver);
        bulkAnalysisPage = loginPage
            .login(requestEntityUtil.getEmbeddedUser())
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);
        String inputRowName = bcmUtil.searchCreateInputRow(requestEntityUtil.getEmbeddedUser(), worksheetResponse, 5);

        worksheetsExplorePage = bulkAnalysisPage.openWorksheet(worksheetResponse.getName());
        softAssertions.assertThat(worksheetsExplorePage.isRemoveButtonEnabled()).isFalse();

        deletePage = worksheetsExplorePage.clickScenarioCheckbox(worksheetResponse.getName())
            .clickRemove();

        softAssertions.assertThat(deletePage.getDeleteText()).contains("You are attempting to remove", "from the bulk analysis. This action cannot be undone.");

        deletePage.removeScenarios(WorksheetsExplorePage.class);
        softAssertions.assertThat(worksheetsExplorePage.isInputRowDisplayed(inputRowName)).isGreaterThan(0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30675, 30676, 30674})
    @Description("Update inputs")
    public void testUpdateInputs() {
        setBulkCostingFlag(true);

        loginPage = new CidAppLoginPage(driver);
        bulkAnalysisPage = loginPage
            .login(requestEntityUtil.getEmbeddedUser())
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);
        bcmUtil.searchCreateInputRow(requestEntityUtil.getEmbeddedUser(), worksheetResponse, 5);

        worksheetsExplorePage = bulkAnalysisPage.openWorksheet(worksheetResponse.getName());

        softAssertions.assertThat(worksheetsExplorePage.isSetInputsEnabled()).isFalse();

        setInputsModalPage = worksheetsExplorePage.clickScenarioCheckbox(worksheetResponse.getName())
            .clickSetInputs();

        setInputsModalPage.selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING)
            // FIXME: 02/07/2024 cn - this df was AGCO Assumption (which should match the assertion) but it doesn't seem to exist in aPD
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_BRAZIL)
            .enterAnnualVolume("100")
            .enterAnnualYears("5")
            .save()
            .clickClose();

        softAssertions.assertThat(worksheetsExplorePage.getRowDetails(worksheetResponse.getName())).contains("2-Model Machining");
        softAssertions.assertThat(worksheetsExplorePage.getRowDetails(worksheetResponse.getName())).contains("AGCO Assumption");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29876, 29875, 29877, 29878, 29924})
    @Description("Edit Bulk Analysis name and search")
    public void editBulkAnalysisNameAndSearch() {
        setBulkCostingFlag(true);

        loginPage = new CidAppLoginPage(driver);
        bulkAnalysisPage = loginPage
            .login(requestEntityUtil.getEmbeddedUser())
            .clickBulkAnalysis();

        WorkSheetResponse worksheetResponse = bcmUtil.createWorksheet(saltName);

        bcmUtil.searchCreateInputRow(requestEntityUtil.getEmbeddedUser(), worksheetResponse, 5);
        softAssertions.assertThat(bulkAnalysisPage.isInfoButtonEnabled()).isTrue();

        bulkAnalysisPage.highlightWorksheet(worksheetResponse.getName())
            .clickInfo();

        String updatedWorksheetName = worksheetResponse.getName().concat("_updated");

        bulkAnalysisInfoPage.enterBulkAnalysisName(updatedWorksheetName)
            .clickSave();

        softAssertions.assertThat(bulkAnalysisPage.isWorksheetPresent(updatedWorksheetName)).isTrue();

        bulkAnalysisPage.enterKeySearch(updatedWorksheetName);

        softAssertions.assertThat(bulkAnalysisPage.getListOfWorksheets()).isEqualTo(1);
        softAssertions.assertThat(bulkAnalysisPage.isWorksheetPresent(updatedWorksheetName)).isTrue();
        softAssertions.assertAll();
    }

    private void setBulkCostingFlag(boolean bulkCostingValue) {
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


