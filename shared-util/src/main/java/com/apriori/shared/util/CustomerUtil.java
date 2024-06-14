package com.apriori.shared.util;

import com.apriori.shared.util.enums.apis.CustomersApiEnum;
import com.apriori.shared.util.enums.apis.DeploymentsAPIEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Applications;
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

    private static String currentCustomerTokenSubject;
    private static String authTargetCloudContext;

    /**
     * Get current customer identity
     */
    public static synchronized String getCurrentCustomerIdentity() {
        return getCustomerData().getIdentity();
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
     * Return token subject for specific customer
     *
     * @return
     */
    public static synchronized String getTokenSubjectForCustomer() {
        return currentCustomerTokenSubject = generateTokenSubject();
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return String - cloud context
     */
    private static String generateAuthTargetCloudContext(UserCredentials userCredentials) {

        final String customerIdentity = getCustomerData().getIdentity();
        final String installationName = PropertiesContext.get("${customer}.multi_tenant_installation_name");
        Deployment deploymentItem = getDeploymentByName(userCredentials, PropertiesContext.get("deployment"));

        Installation installationItem = deploymentItem.getInstallations()
            .stream()

            .filter(element -> element.getName().equalsIgnoreCase(installationName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Could not find installation with name %s\nfor deployment %s",
                        installationName, deploymentItem.getName())
                )
            );

        return customerIdentity +
            deploymentItem.getIdentity() +
            installationItem.getIdentity() +
            getApplicationIdentity();
    }

    /**
     * Calls an API with GET verb
     *
     * @return string
     */
    // TODO: 10/06/2024 cn - this is a duplicate of a cds method which needs to be refactored when cdstestutil is being worked on
    private static String getApplicationIdentity() {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(CustomersApiEnum.APPLICATIONS, Applications.class)
            .queryParams(new QueryParams().use("cloudReference[EQ]", "ci-design"))
            .expectedResponseCode(org.apache.http.HttpStatus.SC_OK);

        ResponseWrapper<Applications> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity().getItems().stream().findFirst().get().getIdentity();
    }

    private static String generateTokenSubject() {
        final Customer customerToProcess = getCustomerData();
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
        List<Deployment> deploymentItems = getDeployments(filterMap);

        return deploymentItems.stream().findFirst().orElseThrow(() -> new RuntimeException("Deployment not found"));
    }

    /**
     * Gets deployments
     *
     * @param queryParams - Map of key value pairs to add to url
     * @return List of Deployment Items
     */
    private static List<Deployment> getDeployments(QueryParams queryParams) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(DeploymentsAPIEnum.DEPLOYMENTS, Deployments.class)
            .inlineVariables(CustomerUtil.getCustomerData().getIdentity())
            .queryParams(queryParams)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Deployments> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity().getItems();
    }

    /**
     * Get customer data by a cloud reference name.  Filter all customers to find a user used for the environment
     *
     * @return Customer object
     */
    public static Customer getCustomerData() {
        return getCustomerData(PropertiesContext.get("customer"));
    }

    /**
     * Get customer data by a cloud reference name.  Filter all customers to find a user used for the environment
     *
     * @return Customer object
     */
    public static Customer getCustomerData(String customer) {
        RequestEntity customerRequest = RequestEntityUtil_Old.init(CustomersApiEnum.CUSTOMERS, Customers.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use("cloudReference[EQ]", customer));

        ResponseWrapper<Customers> customersResponseWrapper = HTTPRequest.build(customerRequest).get();

        return customersResponseWrapper.getResponseEntity().getItems().get(0);
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
