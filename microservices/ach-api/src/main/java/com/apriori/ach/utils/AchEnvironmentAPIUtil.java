package com.apriori.ach.utils;

import com.apriori.ach.dto.ApplicationDTO;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.AccessControlResponse;
import com.apriori.cds.models.response.AccessControls;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AwsParameterStoreUtil;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.Deployments;
import com.apriori.models.response.User;
import com.apriori.models.response.Users;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;

import org.apache.commons.lang.StringUtils;
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
     *
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
     *
     * @param customerIdentity
     * @return customer deployments
     */
    public Deployment getCustomerDeploymentInformation(final String customerIdentity) {
        RequestEntity customerApplicationsRequest = RequestEntityUtil.init(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID, Deployments.class)
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