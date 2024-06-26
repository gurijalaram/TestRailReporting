package com.apriori.ach.ui.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.ach.api.dto.ApplicationDTO;
import com.apriori.ach.api.utils.AchEnvironmentAPIUtil;
import com.apriori.qa.ach.ui.pageobjects.CloudHomeLoginPage;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.ach.ui.utils.AchEnvironmentUIUtil;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.enums.CustomerEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.testrail.TestRail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
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

        cloudHomePage = new CloudHomeLoginPage(driver)
            .login(userCredentials);

        if (cloudHomePage.isSwitchDeploymentButtonExist()) {
            cloudHomePage
                .clickUserPanel()
                .clickSwitchDeploymentButton()
                .clickDeploymentSelector()
                .selectDeployment(deploymentName)
                .clickSubmitButton();
        }

        this.validateDeploymentApplications(cloudHomePage, deploymentName);
    }

    /**
     * Validate applications on the user screen
     * @param cloudHomePage
     * @param deploymentName
     */
    private void validateDeploymentApplications(CloudHomePage cloudHomePage, String deploymentName) {
        final String currentUIDeployment = cloudHomePage.getDeployment();
        assertEquals(deploymentName, currentUIDeployment);

        final String customerIdentity = SharedCustomerUtil.getCustomerData().getIdentity();
        Deployment customerDeployment = achEnvironmentAPIUtil.getCustomerDeploymentInformation(customerIdentity);

        List<ApplicationDTO> mappedCustomerApplications = achEnvironmentAPIUtil.mapCustomerDeploymentDataToDTO(
            customerDeployment
        );
        mappedCustomerApplications.addAll(getMappedMultiTenantApplications());

        List<ApplicationDTO> userApplicationsFromUI = cloudHomePage.getListOfApplications();

        this.validateApplicationsUIText(new ArrayList<>(userApplicationsFromUI), mappedCustomerApplications);
        this.validateApplicationsAreLaunchedSuccessfully(userApplicationsFromUI);
    }

    private List<ApplicationDTO> getMappedMultiTenantApplications() {
        final String apInternalCustomerIdentity = SharedCustomerUtil.getCustomerData(CustomerEnum.AP_INT.getCustomer()).getIdentity();

        return achEnvironmentAPIUtil.mapMultiTenantDeploymentDataToDTO(
            achEnvironmentAPIUtil.getCustomerDeploymentInformation(apInternalCustomerIdentity)
        );
    }

    /**
     * Validate applications text in UI
     * @param applications
     * @param customerApplicationsData
     */
    private void validateApplicationsUIText(List<ApplicationDTO> applications, List<ApplicationDTO> customerApplicationsData) {
        applications.removeAll(customerApplicationsData);
        assertEquals(0, applications.size(), "Applications list should be empty, else application has text representation not related to the customer's environment.\n" +
            "Applications that are not appropriate to customers applications\n" +
            StringUtils.join(applications, '\n')
        );
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

        Assertions.assertTrue(cloudHomePage.getLoadApplicationsErrors().isEmpty(),
            cloudHomePage.getLoadApplicationsErrors()
        );
    }
}
