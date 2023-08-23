package com.integration.tests.customer.util;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.AccessControlResponse;
import com.apriori.cds.models.response.AccessControls;
import com.apriori.cds.models.response.User;
import com.apriori.cds.models.response.Users;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AwsParameterStoreUtil;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.Deployments;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

import java.util.List;

/**
 * Customer environment util class
 * Contains methods with base functionality for customer environments tests
 */
public class CustomerEnvironmentUtil {

    protected UserCredentials getAwsCustomerUserCredentials() {
        final String username = AwsParameterStoreUtil.getSystemParameter("/antman/aPrioriCIGenerateUser");
        final String password = AwsParameterStoreUtil.getSystemParameter("/antman/aPrioriCIGeneratePassword");

        return new UserCredentials(username, password);
    }

    /**
     * Find information about customer user
     * @param email
     * @param customerIdentity
     * @return filtered customer user and all related information
     */
    protected static User getCustomerUserDataByEmail(final String email, final String customerIdentity) {
        RequestEntity customerUsersRequest = RequestEntityUtil.init(CDSAPIEnum.CUSTOMER_USERS, Users.class)
            .inlineVariables(customerIdentity)
            .queryParams(new QueryParams().use("email[EQ]", email))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Users> customerUsersResponse = HTTPRequest.build(customerUsersRequest).get();

        return customerUsersResponse
            .getResponseEntity()
            .getItems().stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Get user access information for a specific customer
     * @param userIdentity
     * @param customerIdentity
     * @return user access information
     */
    protected List<AccessControlResponse> getUserAccessControls(final String userIdentity, final String customerIdentity) {
        RequestEntity userAccessControlRequest = RequestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<AccessControls> serviceAccountControls = HTTPRequest.build(userAccessControlRequest)
            .get();

        return serviceAccountControls.getResponseEntity()
            .getItems();
    }

    /**
     * Get customer deployments and all related objects information
     * @param customerIdentity
     * @return customer deployments
     */
    protected List<Deployment> getCustomerDeployments(final String customerIdentity) {
        RequestEntity customerApplicationsRequest = RequestEntityUtil.init(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID, Deployments.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Deployments> customerApplicationsResponse = HTTPRequest.build(customerApplicationsRequest)
            .get();

        return customerApplicationsResponse.getResponseEntity()
            .getItems();
    }
}