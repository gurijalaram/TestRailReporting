package com.apriori.cid.ui.tests.bulkcosting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.projects.BulkCostingPage;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class BulkCostingPageTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private BulkCostingPage bulkCostingPage;

    @Test
    @TestRail(id = {29187, 29874, 29942})
    @Description("bulk costing page visibility, adding and delete worksheet")
    public void bulkCostingAddAndDeleteWorksheet() {
        UserCredentials userCredentials = UserUtil.getUser();
        SoftAssertions soft = new SoftAssertions();
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        bulkCostingPage = loginPage
            .login(userCredentials)
            .clickBulkCostingButton();

        soft.assertThat(bulkCostingPage.isListOfWorksheetsPresent()).isTrue();

        String name = new GenerateStringUtil().saltString("name");
        BcmUtil bcmUtil = new BcmUtil();
        bcmUtil.createWorksheet(name,userCredentials.getEmail());

        bulkCostingPage.selectAndDeleteSpecificBulkAnalysis(name);
        soft.assertThat(bulkCostingPage.isWorksheetIsPresent(name)).isFalse();
        soft.assertAll();
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


