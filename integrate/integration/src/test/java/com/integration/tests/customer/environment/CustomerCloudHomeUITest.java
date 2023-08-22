package com.integration.tests.customer.environment;


import com.apriori.customer.CloudHomePage;
import com.apriori.customer.dto.ApplicationDataDTO;
import com.apriori.login.LoginService;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Deployment;
import com.apriori.reader.file.user.UserCredentials;

import com.apriori.testrail.TestRail;
import com.integration.tests.customer.util.CustomerEnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerCloudHomeUITest extends CustomerEnvironmentUtil {
    private final UserCredentials userCredentials = getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;
    private LoginService aprioriLoginService;


    @Test
    @TestRail(id = {27951})
    public void validateCustomerApplicationsByUI() {
        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();
        List<Deployment> customerDeployments = getCustomerDeployments(customerIdentity);
        HashMap<String, List<ApplicationDataDTO>> mappedCustomerDeployments =  mapCustomerDeploymentDataToDTO(customerDeployments);

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);

        List<ApplicationDataDTO> applications = cloudHomePage.getListOfApplications();

       List<ApplicationDataDTO> deploymentApplications = mappedCustomerDeployments.get("Production");
       applications.removeAll(deploymentApplications);

        Assertions.assertEquals(0, applications.size(), "Applications list should be empty, else application has an text representation not related to the customers environment.");
    }


    private HashMap<String, List<ApplicationDataDTO>> mapCustomerDeploymentDataToDTO(List<Deployment> customerDeployments) {
        HashMap<String, List<ApplicationDataDTO>> mappedDeploymentData = new HashMap<>();

        customerDeployments.forEach(
            deployment -> {
                List<ApplicationDataDTO> applicationDataDTOS = new ArrayList<>();

                deployment.getInstallations().forEach(
                    installation -> {
                        installation.getApplications().forEach(
                            application -> {
                                ApplicationDataDTO applicationDataDTO = ApplicationDataDTO.builder()
                                    .installation(installation.getName())
                                    .version(installation.getApVersion())
                                    .applicationName(application.getName())
                                    .build();


                                applicationDataDTOS.add(applicationDataDTO);
                            }
                        );
                    }
                );

                mappedDeploymentData.put(deployment.getName(), applicationDataDTOS);
            }
        );

        return mappedDeploymentData;
    }
}
