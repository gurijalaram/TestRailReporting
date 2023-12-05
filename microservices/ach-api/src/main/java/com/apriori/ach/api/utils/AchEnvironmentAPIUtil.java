package com.apriori.ach.api.utils;

import com.apriori.ach.api.dto.ApplicationDTO;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;
import com.apriori.shared.util.properties.PropertiesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * Customer environment util class
 * Contains methods with base functionality for customer environments tests
 */
public class AchEnvironmentAPIUtil extends TestUtil {

    protected final String deploymentName = PropertiesContext.get("${deployment}.name");
    protected final String identitiesDelimiter = "_";

    protected final UserCredentials userCredentials = getAwsCustomerUserCredentials();


    /**
     * Get user credentials for a customer from AWS ParameterStore
     * @return
     */
    public UserCredentials getAwsCustomerUserCredentials() {

        String username = System.getProperty("username");
        String password = System.getProperty("userpass");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            username = AwsParameterStoreUtil.getSystemParameter("/qaautomation/cloudTestUsername1");
            password = AwsParameterStoreUtil.getSystemParameter("/qaautomation/cloudTestUserPass1");
        }

        return new UserCredentials(username, password);
    }

    /**
     * Find information about customer's user
     *
     * @param email
     * @param customerIdentity
     * @return filtered customer user and all related information
     */
    protected static User getCustomerUserDataByEmail(final String email, final String customerIdentity) {
        RequestEntity customerUsersRequest = RequestEntityUtil_Old.init(CDSAPIEnum.CUSTOMER_USERS, Users.class)
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
     *
     * @param userIdentity
     * @param customerIdentity
     * @return user access information
     */
    protected List<AccessControlResponse> getUserAccessControls(final String userIdentity, final String customerIdentity) {
        RequestEntity userAccessControlRequest = RequestEntityUtil_Old.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class)
                .inlineVariables(customerIdentity, userIdentity)
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<AccessControls> serviceAccountControls = HTTPRequest.build(userAccessControlRequest)
                .get();

        return serviceAccountControls.getResponseEntity()
                .getItems();
    }

    /**
     * Get customer deployments and all related objects information
     *
     * @param customerIdentity
     * @return customer deployments
     */
    public Deployment getCustomerDeploymentInformation(final String customerIdentity) {
        RequestEntity customerApplicationsRequest = RequestEntityUtil_Old.init(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID, Deployments.class)
                .inlineVariables(customerIdentity)
                .queryParams(new QueryParams().use("name[EQ]", deploymentName))
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Deployments> customerApplicationsResponse = HTTPRequest.build(customerApplicationsRequest)
                .get();

        return customerApplicationsResponse
                .getResponseEntity()
                .getItems()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Customer deployment was not found. Deployment name: " + deploymentName));
    }



    /**
     * Map customer deployment data into ApplicationDTO class
     * @param customerDeployment
     * @return
     */
    public List<ApplicationDTO> mapCustomerDeploymentDataToDTO(final Deployment customerDeployment) {
        final List<ApplicationDTO> mappedApplicationDTOs = new ArrayList<>();

        customerDeployment.getInstallations().forEach(
                installation -> {
                    installation.getApplications().forEach(
                            application -> {
                                ApplicationDTO applicationDTO = ApplicationDTO.builder()
                                        .identitiesHierarchy(
                                                new StringBuilder("customerId:" + customerDeployment.getCustomerIdentity())
                                                        .append(identitiesDelimiter + "deploymentId:" + customerDeployment.getIdentity())
                                                        .append(identitiesDelimiter + "installationId:" + installation.getIdentity())
                                                        .append(identitiesDelimiter + "applicationId:" + application.getIdentity())
                                                        .toString()
                                        )
                                        .installation(installation.getName())
                                        .version(installation.getApVersion())
                                        .applicationName(application.getName())
                                        .build();

                                mappedApplicationDTOs.add(applicationDTO);
                            }
                    );
                }
        );

        return mappedApplicationDTOs;
    }
}