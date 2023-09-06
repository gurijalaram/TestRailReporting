package com.integration.tests.customer.environment;


import com.apriori.cds.models.response.AccessControlResponse;
import com.apriori.cds.models.response.User;
import com.apriori.models.AuthorizationUtil;
import com.apriori.models.response.Deployment;
import com.apriori.qa.ach.ui.dto.ApplicationDTO;
import com.apriori.qa.integration.utils.CustomerEnvironmentUtil;
import com.apriori.testrail.TestRail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerCloudHomeAPITest extends CustomerEnvironmentUtil {
    @Test
    @TestRail(id = {27011})
    public void validateCustomerApplicationsByAPI() {

        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();
        final User customerUser = getCustomerUserDataByEmail(userCredentials.getEmail(), customerIdentity);

        Deployment deployment = getCustomerDeploymentInformation(customerIdentity);
        List<String> customerReferences = mapCustomerDeploymentDataToDTO(deployment)
                .stream()
                .map(ApplicationDTO::getIdentitiesHierarchy)
                .collect(Collectors.toList());

        List<AccessControlResponse> userAccessControls = getUserAccessControls(customerUser.getIdentity(), customerIdentity);
        List<String> userReferences = this.filterAccessForDeploymentAndConcatIdentities(userAccessControls, deployment.getIdentity());

        userReferences.removeAll(customerReferences);

        Assertions.assertEquals(0, userReferences.size(), "User list should be empty, else user has an access not related to the customers environment.");
    }

    private List<String> filterAccessForDeploymentAndConcatIdentities(List<AccessControlResponse> userAccessControls, final String deploymentIdentity) {
        List<String> userReferences = new ArrayList<>();

        userAccessControls.forEach(
            userAccessControl -> {
                if(userAccessControl.getDeploymentIdentity().equals(deploymentIdentity)) {
                    userReferences.add(
                            new StringBuilder("customerId:" + userAccessControl.getCustomerIdentity())
                                    .append(identitiesDelimiter + "deploymentId:" + userAccessControl.getDeploymentIdentity())
                                    .append(identitiesDelimiter + "installationId:" + userAccessControl.getInstallationIdentity())
                                    .append(identitiesDelimiter + "applicationId:" + userAccessControl.getApplicationIdentity())
                                    .toString()
                    );
                }
            }
        );

        return userReferences;
    }
}
