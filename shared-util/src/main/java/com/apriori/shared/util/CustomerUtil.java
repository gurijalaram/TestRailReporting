package com.apriori.shared.util;

import com.apriori.shared.util.enums.apis.CustomersApiEnum;
import com.apriori.shared.util.enums.apis.DeploymentsAPIEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Application;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Customers;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.models.response.Installation;
import com.apriori.shared.util.models.response.Sites;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomerUtil {

    private static Customer currentCustomer;
    private static Customer apIntCustomer;
    private static String currentCustomerIdentity;
    private static String currentCustomerTokenSubject;
    private static String authTargetCloudContext;

    /**
     * Get current customer identity
     */
    public static synchronized String getCurrentCustomerIdentity() {
        return Objects.requireNonNullElseGet(currentCustomerIdentity, () -> {
            try {
                // TODO : should be removed when AWS data will be available for staging too
                currentCustomerIdentity = PropertiesContext.get("${customer}.${env}.customer_identity");
            } catch (IllegalArgumentException e) {
                currentCustomerIdentity = getCurrentCustomerData().getIdentity();
            }

            return currentCustomerIdentity;
        });
    }

    /**
     * Get Apriori customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     *
     * @return filtered customer
     */
    public static synchronized Customer getApIntCustomerData() {
        return Objects.requireNonNullElseGet(apIntCustomer, () -> apIntCustomer = getCustomerData("ap-int"));
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return String - cloud context
     */
    public static synchronized String getAuthTargetCloudContext(UserCredentials userCredentials) {
        return Objects.requireNonNullElseGet(authTargetCloudContext, () -> authTargetCloudContext = generateAuthTargetCloudContext(userCredentials));
    }

    /**
     * Get current customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     *
     * @return filtered customer
     */
    public static synchronized Customer getCurrentCustomerData() {
        return Objects.requireNonNullElseGet(currentCustomer, () -> currentCustomer = getCustomerData(null));
    }

    /**
     * Return token subject for specific customer
     *
     * @return
     */
    public static synchronized String getTokenSubjectForCustomer() {
        if (currentCustomerTokenSubject != null) {
            return currentCustomerTokenSubject;
        }

        try {
            // TODO : should be removed when AWS data will be available for staging too
            currentCustomerTokenSubject = PropertiesContext.get("${customer}.${env}.token_subject");
        } catch (IllegalArgumentException e) {
            currentCustomerTokenSubject = generateTokenSubject();
        }

        return currentCustomerTokenSubject;
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return String - cloud context
     */
    private static String generateAuthTargetCloudContext(UserCredentials userCredentials) {

        final String customerIdentity = getCurrentCustomerIdentity();
        final String installationName = PropertiesContext.get("${env}.multi_tenant_installation_name");
        final String applicationNameFromConfig = getApplicationName();

        Deployment deploymentItem = getDeploymentByName(userCredentials, PropertiesContext.get("deployment"));

        Installation installationItem = deploymentItem.getInstallations()
            .stream()
            .filter(element -> element.getName().equals(installationName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Could not find installation with name %s\nfor deployment %s",
                        installationName, deploymentItem.getName())
                )
            );

        Application applicationItem = installationItem.getApplications()
            .stream()
            .filter(element -> element.getServiceName().equalsIgnoreCase(applicationNameFromConfig))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Could not find application with name %s\nfor installation %s\nand deployment %s",
                        applicationNameFromConfig, installationName, deploymentItem.getName())
                )
            );

        return new StringBuilder(customerIdentity)
            .append(deploymentItem.getIdentity())
            .append(installationItem.getIdentity())
            .append(applicationItem.getIdentity())
            .toString();
    }

    private static String generateTokenSubject() {
        final Customer customerToProcess = getCurrentCustomerData();
        final String customerSiteId = getCustomerSiteIdByCustomer(customerToProcess);

        if (StringUtils.isBlank(customerSiteId)) {
            log.error("Customer site id is empty. Customer: {}", customerToProcess.getCloudReference());
        }

        return customerSiteId.substring(customerSiteId.length() - 4);
    }

    /**
     * Gets deployments response object to allow for usage of response
     *
     * @param userCredentials UserCredentials instance containing user details to use in api call
     * @return GetDeploymentsResponse instance
     */
    private static Deployment getDeploymentByName(UserCredentials userCredentials, String deploymentName) {
        QueryParams filterMap = new QueryParams();
        filterMap.put("name[EQ]", deploymentName);
        List<Deployment> deploymentItems = getDeploymentItems(userCredentials, filterMap);
        return deploymentItems.stream().findFirst().orElseThrow(() -> new RuntimeException("Deployment not found"));
    }

    /**
     * Gets deployments
     *
     * @param userCredentials - UserCredentials instance containing user details to use in api call
     * @param queryParams     - Map of key value pairs to add to url
     * @return List of Deployment Items
     */
    private static List<Deployment> getDeploymentItems(UserCredentials userCredentials, QueryParams queryParams) {
        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(DeploymentsAPIEnum.DEPLOYMENTS, Deployments.class)
            .token(userCredentials.getToken())
            .inlineVariables(
                CustomerUtil.getApIntCustomerData().getIdentity()
            )
            .queryParams(queryParams)
            .expectedResponseCode(HttpStatus.SC_OK);

        return ((Deployments) HTTPRequest.build(requestEntity)
            .get()
            .getResponseEntity()
        ).getItems();
    }

    private static String getApplicationName() {
        try {
            return PropertiesContext.get("application_name");
        } catch (IllegalArgumentException e) {
            return "cid";
        }
    }

    /**
     * Get customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     *
     * @param customerName - if null will return current customer data
     * @return
     */
    public static Customer getCustomerData(final String customerName) {
        String customerCloudReferenceName = getCustomerCloudReferenceName(customerName);

        RequestEntity customerRequest = RequestEntityUtil_Old.init(CustomersApiEnum.CUSTOMERS, Customers.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use("cloudReference[EQ]", customerCloudReferenceName));

        ResponseWrapper<Customers> customersResponseWrapper = HTTPRequest.build(customerRequest).get();

        return customersResponseWrapper.getResponseEntity().getItems().get(0);
    }

    private static String getCustomerCloudReferenceName(final String customerName) {
        try {
            return customerName == null
                ? PropertiesContext.get("${customer}.cloud_reference_name")
                : PropertiesContext.get(String.format("%s.cloud_reference_name", customerName));
        } catch (IllegalArgumentException e) {
            log.info("${customer}.cloud_reference_name is not specified. Setting it to be the same as customer name");
            return PropertiesContext.get("customer");
        }
    }

    public static String getCustomerSiteIdByCustomer(Customer customerToProcess) {
        RequestEntity sitesRequest = RequestEntityUtil_Old.init(CustomersApiEnum.SITES_BY_CUSTOMER_ID, Sites.class)
            .inlineVariables(customerToProcess.getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Sites> sitesResponseWrapper = HTTPRequest.build(sitesRequest).get();

        return sitesResponseWrapper.getResponseEntity()
            .getItems()
            .get(0)
            .getSiteId();
    }
}
