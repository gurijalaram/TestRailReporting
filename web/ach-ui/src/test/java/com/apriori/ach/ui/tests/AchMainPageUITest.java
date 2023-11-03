package com.apriori.ach.ui.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.ach.api.dto.ApplicationDTO;
import com.apriori.ach.api.utils.AchEnvironmentAPIUtil;
import com.apriori.qa.ach.ui.pageobjects.CloudHomeLoginPage;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.ach.ui.utils.AchEnvironmentUIUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.AuthorizationUtil;
import com.apriori.shared.util.testrail.TestRail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AchMainPageUITest extends AchEnvironmentUIUtil {
    private AchEnvironmentAPIUtil achEnvironmentAPIUtil = new AchEnvironmentAPIUtil();
    private final UserCredentials userCredentials = achEnvironmentAPIUtil.getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;

    @Test
    @TestRail(id = {27951})
    public void validateCustomerApplicationsByUI() {
        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();

        List<ApplicationDTO> mappedCustomerApplications = achEnvironmentAPIUtil.mapCustomerDeploymentDataToDTO(
                achEnvironmentAPIUtil.getCustomerDeploymentInformation(customerIdentity)
        );

        cloudHomePage = new CloudHomeLoginPage(driver)
            .login(userCredentials)
            .clickUserPanel()
            .clickSwitchDeploymentButton()
            .clickDeploymentSelector()
            .selectDeployment(deploymentName)
            .clickSubmitButton();

        this.validateDeploymentApplications(cloudHomePage, mappedCustomerApplications, deploymentName);
    }

    /**
     * Validate applications on the user screen
     * @param cloudHomePage
     * @param mappedCustomerApplications
     * @param deploymentName
     */
    private void validateDeploymentApplications(CloudHomePage cloudHomePage, List<ApplicationDTO> mappedCustomerApplications, String deploymentName) {
        String currentUIDeployment = cloudHomePage.getDeployment();

        assertEquals(deploymentName, currentUIDeployment);

        List<ApplicationDTO> userApplicationsFromUI = cloudHomePage.getListOfApplications();

        this.validateApplicationsUIText(new ArrayList<>(userApplicationsFromUI), mappedCustomerApplications);
        this.validateApplicationsAreLaunchedSuccessfully(userApplicationsFromUI);
    }

    /**
     * Validate applications text in UI
     * @param applications
     * @param customerApplicationsData
     */
    private void validateApplicationsUIText(List<ApplicationDTO> applications, List<ApplicationDTO> customerApplicationsData) {
        applications.removeAll(customerApplicationsData);
        assertEquals(0, applications.size(), "Applications list should be empty, else application has an text representation not related to the customers environment.");
    }

    /**
     * Click on each application on the user screen and validate that application is launched
     * @param userApplicationsFromUI
     */
    private void validateApplicationsAreLaunchedSuccessfully(List<ApplicationDTO> userApplicationsFromUI) {

        userApplicationsFromUI.forEach(application -> {
            log.debug("*********************** Testing application name: {}  **********************", application.getApplicationName());

            cloudHomePage.clickWebApplicationByNameAndCloseAfterLoad(application.getApplicationName(),
                    getPageObjectTypeByApplicationName(application.getApplicationName())
            );
        });
    }
}
