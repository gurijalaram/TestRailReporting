package com.apriori.cid.ui.tests.bulkcosting;

import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.projects.BulkCostingPage;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BulkCostingPageTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private BulkCostingPage bulkCostingPage;
    private String worksheetIdentity;
    private UserCredentials userCredentials = UserUtil.getUser();

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
            .clickBulkCostingButton();

        soft.assertThat(bulkCostingPage.isListOfWorksheetsPresent()).isTrue();

        ResponseWrapper<WorkSheetResponse> worksheetResponse = createWorksheet(userCredentials);

        bulkCostingPage.selectAndDeleteSpecificBulkAnalysis(worksheetResponse.getResponseEntity().getName());
        soft.assertThat(bulkCostingPage.isWorksheetIsPresent(worksheetResponse.getResponseEntity().getName())).isFalse();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30730})
    @Description("create inputRow for the worksheet")
    public void createInputRow() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkCostingButton();

        ResponseWrapper<WorkSheetResponse> worksheetResponse = createWorksheet(userCredentials);
        String inputRowName = createInputRow(userCredentials, worksheetResponse);
        bulkCostingPage.enterSpecificBulkAnalysis(worksheetResponse.getResponseEntity().getName());
        soft.assertThat(bulkCostingPage.isInputRowDisplayed(inputRowName)).isTrue();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30679, 30680, 30681, 30682, 30684})
    @Description("delete input row for the worksheet")
    public void deleteInputRow() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkCostingButton();

        ResponseWrapper<WorkSheetResponse> worksheetResponse = createWorksheet(userCredentials);
        String inputRowName = createInputRow(userCredentials, worksheetResponse);
        bulkCostingPage.enterSpecificBulkAnalysis(worksheetResponse.getResponseEntity().getName());
        soft.assertThat(bulkCostingPage.getRemoveButtonState())
            .contains(Arrays.asList("Cannot perform a remove action with no scenarios selected"));

        bulkCostingPage.selectFirstPartInWorkSheet();
        soft.assertThat(bulkCostingPage.getRemoveButtonState())
            .isEqualTo("Remove");
        soft.assertThat(bulkCostingPage.clickOnRemoveButtonAngGetConfirmationText())
            .contains(Arrays.asList("You are attempting to remove", "from the bulk analysis. This action cannot be undone."));

        bulkCostingPage.clickOnRemoveScenarioButtonOnConfirmationScreen();
        soft.assertThat(bulkCostingPage.IfScenarioIsPresentOnPage(inputRowName));
        soft.assertAll();
    }

    private String createInputRow(UserCredentials userCredentials, ResponseWrapper<WorkSheetResponse> worksheetResponse) {
        CssComponent cssComponent = new CssComponent();
        BcmUtil bcmUtil = new BcmUtil();
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(userCredentials, "PART")
                .getResponseEntity().getItems().get(5);
        bcmUtil.createWorkSheetInputRowWithEmail(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(), worksheetResponse.getResponseEntity().getIdentity(), userCredentials);
        return scenarioItem.getComponentDisplayName();
    }

    private ResponseWrapper<WorkSheetResponse> createWorksheet(UserCredentials userCredentials) {
        String name = new GenerateStringUtil().saltString("name");
        BcmUtil bcmUtil = new BcmUtil();
        ResponseWrapper<WorkSheetResponse> response =
            bcmUtil.createWorksheetWithEmail(name, UserCredentials.init(userCredentials.getEmail(), null));
        worksheetIdentity = response.getResponseEntity().getIdentity();
        return response;
    }

    private void setBulkCostingFlag(boolean bulkCostingValue) {
        CdsTestUtil cdsTestUtil = new CdsTestUtil();
        Customer customer;
        customer = cdsTestUtil.getAprioriInternal();
        String customerIdentity = customer.getIdentity();

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

        cdsTestUtil.updateFeature(customerIdentity, deploymentIdentity, installationIdentity, bulkCostingValue);
    }
}


