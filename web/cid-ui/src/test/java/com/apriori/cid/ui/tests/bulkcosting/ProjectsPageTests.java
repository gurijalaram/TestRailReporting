package com.apriori.cid.ui.tests.bulkcosting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.projects.ProjectsPage;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ProjectsPageTests extends TestBaseUI {
    private CidAppLoginPage loginPage;
    private ProjectsPage projectsPage;
    private ExplorePage explorePage;

    @AfterEach
    public void cleanup() {
        setBulkCostingFlag(false);
    }

    @Test
    @TestRail(id = 29187)
    @Description("when feature flag BULK_COSTING is set to true projects page should be accessible")
    public void projectPageVisible() {
        setBulkCostingFlag(true);
        loginPage = new CidAppLoginPage(driver);
        projectsPage = loginPage
                .login(UserUtil.getUser())
                .clickProjectsButton();

        assertTrue(projectsPage.isOnProjectsPage());
    }

    @Test
    @TestRail(id = 29188)
    @Description("when feature flag BULK_COSTING is set to false projects page should not be accessible")
    public void projectPageNotVisible() {
        setBulkCostingFlag(false);
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage
            .login(UserUtil.getUser());

        assertFalse(explorePage.isProjectsButtonVisible());
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

        cdsTestUtil.updateFeature(customerIdentity, deploymentIdentity, installationIdentity, false, bulkCostingValue);
    }
}


