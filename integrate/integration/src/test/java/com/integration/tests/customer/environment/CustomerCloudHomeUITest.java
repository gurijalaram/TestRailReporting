package com.integration.tests.customer.environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.login.LoginService;
import com.apriori.models.AuthorizationUtil;
import com.apriori.qa.ach.ui.dto.ApplicationDTO;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.integration.utils.CustomerEnvironmentUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.testrail.TestRail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerCloudHomeUITest extends CustomerEnvironmentUtil {
    private final UserCredentials userCredentials = getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;
    private LoginService aprioriLoginService;

    @Test
    @TestRail(id = {27951})
    public void validateCustomerApplicationsByUI() {
        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();

        List<ApplicationDTO> mappedCustomerApplications = mapCustomerDeploymentDataToDTO(
                getCustomerDeploymentInformation(customerIdentity)
        );

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);


        cloudHomePage.clickUserPanel()
                .clickSwitchDeploymentButton()
                .clickDeploymentSelector()
                .selectDeployment(deploymentName)
                .clickSubmitButton();

        this.validateDeploymentApplications(cloudHomePage, mappedCustomerApplications, deploymentName);
    }

    private void validateDeploymentApplications(CloudHomePage cloudHomePage, List<ApplicationDTO> mappedCustomerApplications, String deploymentName) {
        String currentUIDeployment = cloudHomePage.getDeployment();

        assertEquals(deploymentName, currentUIDeployment);

        List<ApplicationDTO> userApplicationsFromUI = cloudHomePage.getListOfApplications();

        this.validateApplicationsUIText(new ArrayList<>(userApplicationsFromUI), mappedCustomerApplications);
        this.validateApplicationsAreLaunchedSuccessfully(userApplicationsFromUI);
    }

    private void validateApplicationsUIText(List<ApplicationDTO> applications, List<ApplicationDTO> customerApplicationsData) {
        applications.removeAll(customerApplicationsData);
        assertEquals(0, applications.size(), "Applications list should be empty, else application has an text representation not related to the customers environment.");
    }

    private void validateApplicationsAreLaunchedSuccessfully(List<ApplicationDTO> userApplicationsFromUI) {

        userApplicationsFromUI.forEach(application -> {
            log.debug("*********************** Testing application name: {}  **********************", application.getApplicationName());

            cloudHomePage.clickWebApplicationByName(application.getApplicationName(),
                    getPageObjectTypeByApplicationName(application.getApplicationName())
            );
        });
    }
}
