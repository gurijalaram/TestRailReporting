package com.apriori.cid.ui.tests.bulkcosting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.projects.BulkCostingPage;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
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

import java.util.List;

public class BulkCostingPageTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private BulkCostingPage bulkCostingPage;
    private String worksheetIdentity;
    private UserCredentials userCredentials = UserUtil.getUser();

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            BcmUtil bcmUtil = new BcmUtil();
            bcmUtil.deleteWorksheetWithEmail(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT,UserCredentials.init(userCredentials.getEmail(), null));
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

        String name = new GenerateStringUtil().saltString("name");
        BcmUtil bcmUtil = new BcmUtil();
        bcmUtil.createWorksheetWithEmail(name,UserCredentials.init(userCredentials.getEmail(), null));

        bulkCostingPage.selectAndDeleteSpecificBulkAnalysis(name);
        soft.assertThat(bulkCostingPage.isWorksheetIsPresent(name)).isFalse();
        soft.assertAll();
    }

    @Test
    @TestRail(id = {30599})
    @Description("edit parts in worksheet")
    public void editPartsInWorksheet() {
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);

        String name = createWorksheet();

        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkCostingButton()
            .enterSpecificBulkAnalysis(name)
            .selectFirstPartInWorkSheet()
            .clickEditButton();

        soft.assertThat(bulkCostingPage.isOnEditPage()).isTrue();
        soft.assertThat(bulkCostingPage.isEditSuccessful()).isTrue();
        soft.assertAll();
    }

    private String  createWorksheet() {
        CssComponent cssComponent = new CssComponent();
        final String componentType = "PART";
        ScenarioItem scenarioItem = cssComponent.postSearchRequestCustomParam(userCredentials, componentType,"scenarioPublished[EQ]","true")
            .getResponseEntity().getItems().get(0);

        String name = new GenerateStringUtil().saltString("name");
        BcmUtil bcmUtil = new BcmUtil();

        worksheetIdentity =
            bcmUtil.createWorksheetWithEmail(name,UserCredentials.init(userCredentials.getEmail(), null)).getResponseEntity().getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow =
            bcmUtil.createWorkSheetInputRowWithEmail(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity,
                UserCredentials.init(userCredentials.getEmail(), null));
        return name;
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


