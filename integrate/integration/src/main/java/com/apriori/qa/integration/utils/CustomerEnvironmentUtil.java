package com.apriori.qa.integration.utils;

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
import com.apriori.login.CommonLoginPageImplementation;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.Deployments;
import com.apriori.pageobjects.customeradmin.CustomerAdminPage;
import com.apriori.pageobjects.header.ReportsHeader;
import com.apriori.pageobjects.homepage.AdminHomePage;
import com.apriori.pageobjects.messages.MessagesPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.properties.PropertiesContext;
import com.apriori.qa.ach.ui.dto.ApplicationDTO;
import com.apriori.qa.ach.ui.pageobjects.applications.AppStreamPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.testconfig.TestBaseUI;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Customer environment util class
 * Contains methods with base functionality for customer environments tests
 */
public class CustomerEnvironmentUtil extends TestBaseUI {

    protected final String deploymentName = PropertiesContext.get("${deployment}.name");
    protected final String identitiesDelimiter = "_";

    private static final Map<String, Class<? extends LoadableComponent>> APPLICATIONS_CLASS = new LinkedHashMap<>() {{
            put("aP Admin", AdminHomePage.class);
            put("aP Analytics", ReportsHeader.class);
            put("aP Connect", WorkflowHome.class);
            put("aP Design", CommonLoginPageImplementation.class);
            put("aP Pro", AppStreamPage.class);
            put("aP Workspace", MessagesPage.class);
            put("Customer Admin", CustomerAdminPage.class);
            put("Electronics Data Collection", CommonLoginPageImplementation.class);
        }};

    protected final UserCredentials userCredentials = getAwsCustomerUserCredentials();


    /**
     * Get user credentials for a customer from AWS ParameterStore
     * @return
     */
    protected UserCredentials getAwsCustomerUserCredentials() {

        // TODO z: update to use propertyContext with Null value
        String username = System.getProperty("username");
        String password = System.getProperty("userpass");

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
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
    protected Deployment getCustomerDeploymentInformation(final String customerIdentity) {
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
     * Specify PageObject type based on an application name
     * @param applicationName
     * @return
     */
    protected Class<? extends LoadableComponent> getPageObjectTypeByApplicationName(final String applicationName) {
        if (APPLICATIONS_CLASS.containsKey(applicationName)) {
            return APPLICATIONS_CLASS.get(applicationName);
        }

        throw new IllegalArgumentException("Application to open is not supported. Application name:" + applicationName);

    }

    /**
     * Map customer deployment data into ApplicationDTO class
     * @param customerDeployment
     * @return
     */
    protected List<ApplicationDTO> mapCustomerDeploymentDataToDTO(final Deployment customerDeployment) {
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