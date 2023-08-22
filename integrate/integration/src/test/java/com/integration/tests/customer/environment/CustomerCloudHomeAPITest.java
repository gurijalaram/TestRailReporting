package com.integration.tests.customer.environment;


import com.apriori.cds.models.response.AccessControlResponse;
import com.apriori.cds.models.response.User;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Deployment;
import com.apriori.testrail.TestRail;

import com.integration.tests.customer.util.CustomerEnvironmentUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CustomerCloudHomeAPITest extends CustomerEnvironmentUtil {
    @Test
    @TestRail(id = {27011})
    public void validateCustomerApplicationsByAPI() {
        final String delimiter = "_";

        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();
        final User customerUser = getCustomerUserDataByEmail(userCredentials.getEmail(), customerIdentity);

        List<AccessControlResponse> userAccessControls = getUserAccessControls(customerUser.getIdentity(), customerIdentity);
        List<Deployment> customerDeployments = getCustomerDeployments(customerIdentity);

        List<String> customerReferences = this.concatCustomerIdentitiesToStringList(customerDeployments, delimiter);
        List<String> userReferences = this.concatUserIdentitiesToStringList(userAccessControls, delimiter);

        userReferences.removeAll(customerReferences);

        Assertions.assertEquals(0, userReferences.size(), "User list should be empty, else user has an access not related to the customers environment.");
    }

    private List<String> concatUserIdentitiesToStringList(List<AccessControlResponse> userAccessControls, String delimiter) {
        List<String> userReferences = new ArrayList<>();

        userAccessControls.forEach(
            userAccessControl -> {
                userReferences.add(
                    new StringBuilder(userAccessControl.getCustomerIdentity())
                        .append(delimiter)
                        .append(userAccessControl.getDeploymentIdentity())
                        .append(delimiter)
                        .append(userAccessControl.getInstallationIdentity())
                        .append(delimiter)
                        .append(userAccessControl.getApplicationIdentity())
                        .toString()
                );
            }
        );

        return userReferences;
    }

    private List<String> concatCustomerIdentitiesToStringList(List<Deployment> customerDeployments, final String delimiter) {
        List<String> customerReferences = new ArrayList<>();

        customerDeployments.forEach(
            deployment -> {
                StringBuilder deploymentIdentity = new StringBuilder(deployment.getCustomerIdentity())
                    .append(delimiter)
                    .append(deployment.getIdentity());

                deployment.getInstallations().forEach(
                    installation -> {
                        StringBuilder installationIdentity = new StringBuilder(deploymentIdentity)
                            .append(delimiter)
                            .append(installation.getIdentity());

                        installation.getApplications().forEach(
                            application -> {
                                customerReferences.add(new StringBuilder(installationIdentity)
                                    .append(delimiter)
                                    .append(application.getIdentity())
                                    .toString()
                                );
                            }
                        );
                    }
                );
            }
        );

        return customerReferences;
    }
}
