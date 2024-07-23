package com.apriori.ach.api.tests;


import com.apriori.ach.api.dto.ApplicationDTO;
import com.apriori.ach.api.utils.AchEnvironmentAPIUtil;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.testrail.TestRail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AchMainPageAPITests {
    private AchEnvironmentAPIUtil achEnvironmentAPIUtil;
    private RequestEntityUtil requestEntityUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser().useTokenInRequests();
        achEnvironmentAPIUtil = new AchEnvironmentAPIUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {27011})
    public void validateCustomerApplicationsByAPI() {

        final String customerIdentity = SharedCustomerUtil.getCurrentCustomerIdentity();
        final User customerUser = achEnvironmentAPIUtil.getCustomerUserDataByEmail(requestEntityUtil.getEmbeddedUser().getEmail(), customerIdentity);

        Deployment deployment = achEnvironmentAPIUtil.getCustomerDeploymentInformation(customerIdentity);
        List<String> customerReferences = achEnvironmentAPIUtil.mapCustomerDeploymentDataToDTO(deployment)
                .stream()
                .map(ApplicationDTO::getIdentitiesHierarchy)
                .toList();

        List<AccessControlResponse> userAccessControls = achEnvironmentAPIUtil.getUserAccessControls(customerUser.getIdentity(), customerIdentity);
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
                                    .append(achEnvironmentAPIUtil.identitiesDelimiter + "deploymentId:" + userAccessControl.getDeploymentIdentity())
                                    .append(achEnvironmentAPIUtil.identitiesDelimiter + "installationId:" + userAccessControl.getInstallationIdentity())
                                    .append(achEnvironmentAPIUtil.identitiesDelimiter + "applicationId:" + userAccessControl.getApplicationIdentity())
                                    .toString()
                    );
                }
            }
        );

        return userReferences;
    }
}
