package com.integration.tests.customer.environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.login.LoginService;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Deployment;
import com.apriori.qa.ach.ui.dto.ApplicationDTO;
import com.apriori.qa.ach.ui.enums.CustomerDeploymentsEnum;
import com.apriori.qa.ach.ui.pageobjects.CloudHomePage;
import com.apriori.qa.integration.utils.CustomerEnvironmentUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.testrail.TestRail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<Deployment> customerDeployments = getCustomerDeployments(customerIdentity);
        HashMap<String, List<ApplicationDTO>> mappedCustomerDeployments = mapCustomerDeploymentDataToDTO(customerDeployments);

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);


        CustomerDeploymentsEnum deploymentToTest = CustomerDeploymentsEnum.PRODUCTION;
        cloudHomePage.clickUserPanel()
            .clickSwitchDeploymentButton()
            .clickDeploymentSelector()
            .selectDeployment(deploymentToTest)
            .clickSubmitButton();

        this.validateDeploymentApplications(cloudHomePage, mappedCustomerDeployments, deploymentToTest);
    }

    private void validateDeploymentApplications(CloudHomePage cloudHomePage, HashMap<String, List<ApplicationDTO>> mappedCustomerDeployments, CustomerDeploymentsEnum customerDeploymentsEnum) {
        String currentUIDeployment = cloudHomePage.getDeployment();

        assertEquals(customerDeploymentsEnum.getDeploymentName(), currentUIDeployment);

        List<ApplicationDTO> userApplicationsFromUI = cloudHomePage.getListOfApplications();

        this.validateApplicationsUIText(new ArrayList<>(userApplicationsFromUI), mappedCustomerDeployments.get(currentUIDeployment));
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


    private HashMap<String, List<ApplicationDTO>> mapCustomerDeploymentDataToDTO(List<Deployment> customerDeployments) {
        HashMap<String, List<ApplicationDTO>> mappedDeploymentData = new HashMap<>();

        customerDeployments.forEach(
            deployment -> {
                List<ApplicationDTO> applicationDTOS = new ArrayList<>();

                deployment.getInstallations().forEach(
                    installation -> {
                        installation.getApplications().forEach(
                            application -> {
                                ApplicationDTO applicationDTO = ApplicationDTO.builder()
                                    .installation(installation.getName())
                                    .version(installation.getApVersion())
                                    .applicationName(application.getName())
                                    .build();


                                applicationDTOS.add(applicationDTO);
                            }
                        );
                    }
                );

                mappedDeploymentData.put(deployment.getName(), applicationDTOS);
            }
        );

        return mappedDeploymentData;
    }
}
