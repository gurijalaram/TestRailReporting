package com.integration.tests.customer.environment;


import com.apriori.customer.CloudHomePage;
import com.apriori.customer.dto.ApplicationDataDTO;
import com.apriori.login.LoginService;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Deployment;
import com.apriori.reader.file.user.UserCredentials;

import com.integration.tests.customer.util.CustomerEnvironmentUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerCloudHomeUITest extends CustomerEnvironmentUtil {
    private final UserCredentials userCredentials = getAwsCustomerUserCredentials();
    private CloudHomePage cloudHomePage;
    private LoginService aprioriLoginService;


    @Test
    //@TestRail(id = {27951})
    public void validateCustomerApplicationsByUI() {
        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();
        //        final User customerUser = getCustomerUserDataByEmail(userCredentials.getEmail(), customerIdentity);
        //        List<AccessControlResponse> userAccessControls = getUserAccessControls(customerUser.getIdentity(), customerIdentity);
        List<Deployment> customerDeployments = getCustomerDeployments(customerIdentity);
        HashMap<String, List<ApplicationDataDTO>> mappedCustomerDeployments =  mapCustomerDeploymentDataToDTO(customerDeployments);

        aprioriLoginService = new LoginService(driver, "");
        cloudHomePage = aprioriLoginService.login(userCredentials, CloudHomePage.class);

        List<ApplicationDataDTO> applications = cloudHomePage.getListOfApplications();
        System.out.println();


//        cloudHomePage.getListOfApplications().forEach(webApplicationFromUI -> {
//
//                customerDeployments.forEach(deployment -> {
//                    deployment.getInstallations().stream()
//                        .filter(installation -> installation.getName().equals(webApplicationFromUI.getInstallation())
//                            && installation.getApVersion().equals(webApplicationFromUI.getVersion())
//                        ).findFirst()
//                        .orElseThrow(() -> new IllegalArgumentException(String.format("Installation %s\nWith version %s\nFor deployment %s\nWas not found.",
//                            webApplicationFromUI.getApplicationName(), webApplicationFromUI.getInstallation(), webApplicationFromUI.getVersion(), deployment.getName()))
//                        )
//                        .getApplications().stream()
//                        .filter(application -> application.getName().equals(webApplicationFromUI.getApplicationName()))
//                        .findFirst()
//                        .orElseThrow(() -> new IllegalArgumentException(String.format("Web application %s\nFor installation %s\nWith version %s\nFor deployment %s\nWas not found.",
//                            webApplicationFromUI.getApplicationName(), webApplicationFromUI.getInstallation(), webApplicationFromUI.getVersion(), deployment.getName()))
//                        );
//                });
//
//                //                cloudHomePage.clickWebApplicationByName(webApplicationFromUI.getApplicationName(), CloudHomePage.class).goToProfilePage();
//            }
//        );
    }


    private HashMap<String, List<ApplicationDataDTO>> mapCustomerDeploymentDataToDTO(List<Deployment> customerDeployments) {
        HashMap<String, List<ApplicationDataDTO>> mappedDeploymentData = new HashMap<>();

        customerDeployments.forEach(
            deployment -> {
                List<ApplicationDataDTO> applicationDataDTOS = new ArrayList<>();

                deployment.getInstallations().forEach(
                    installation -> {
                        ApplicationDataDTO applicationDataDTO = ApplicationDataDTO.builder()
                            .installation(installation.getName())
                            .version(installation.getApVersion())
                            .build();

                        installation.getApplications().forEach(
                            application -> {
                                applicationDataDTO.setApplicationName(application.getName());
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
