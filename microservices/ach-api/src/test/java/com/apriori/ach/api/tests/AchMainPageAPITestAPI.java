package com.apriori.ach.api.tests;


import com.apriori.ach.api.dto.ApplicationDTO;
import com.apriori.ach.api.utils.AchEnvironmentAPIUtil;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.shared.util.models.AuthorizationUtil;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.testrail.TestRail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AchMainPageAPITestAPI extends AchEnvironmentAPIUtil {
    @Test
    @TestRail(id = {27011})
    public void validateCustomerApplicationsByAPI() {

        final String customerIdentity = AuthorizationUtil.getCurrentCustomerData().getIdentity();
        final User customerUser = AchEnvironmentAPIUtil.getCustomerUserDataByEmail(userCredentials.getEmail(), customerIdentity);

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

    /**
     * Filter user access data based on environment <br>
     * Map identities into string for future compare
     * @param userAccessControls
     * @param deploymentIdentity
     * @return
     */
    private List<String> filterAccessForDeploymentAndConcatIdentities(List<AccessControlResponse> userAccessControls, final String deploymentIdentity) {
        List<String> userReferences = new ArrayList<>();

        userAccessControls.forEach(
            userAccessControl -> {
                if (userAccessControl.getDeploymentIdentity().equals(deploymentIdentity)) {
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
