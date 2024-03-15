package com.apriori.shared.util.models;

import com.apriori.shared.util.enums.CustomersApiEnum;
import com.apriori.shared.util.enums.DeploymentsAPIEnum;
import com.apriori.shared.util.enums.TokenEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.request.TokenRequest;
import com.apriori.shared.util.models.response.Application;
import com.apriori.shared.util.models.response.Claims;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.Customers;
import com.apriori.shared.util.models.response.Deployment;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.models.response.Installation;
import com.apriori.shared.util.models.response.Sites;
import com.apriori.shared.util.models.response.Token;
import com.apriori.shared.util.models.response.TokenInformation;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthorizationUtil {
    private static String tokenSubject;
    private static Customer currentCustomer;

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    public synchronized ResponseWrapper<Token> getToken(UserCredentials userCredentials) {
        log.info("Getting ATS Token...");

        RequestEntity requestEntity = RequestEntityUtil_Old.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(PropertiesContext.get("ats.token_issuer"))
                    .subject(getTokenSubjectForCustomer())
                    .claims(Claims.builder()
                        .name(userCredentials.getUsername())
                        .email(userCredentials.getEmail())
                        .build())
                    .build())
                .build())
            .expectedResponseCode(HttpStatus.SC_CREATED);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets deployments
     *
     * @param userCredentials - UserCredentials instance containing user details to use in api call
     * @param queryParams     - Map of key value pairs to add to url
     * @return List of Deployment Items
     */
    private List<Deployment> getDeploymentItems(UserCredentials userCredentials, QueryParams queryParams) {
        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(DeploymentsAPIEnum.DEPLOYMENTS, Deployments.class)
            .token(userCredentials.getToken())
            .inlineVariables(
                PropertiesContext.get("${customer}.${env}.customer_identity")
            )
            .queryParams(queryParams)
            .expectedResponseCode(HttpStatus.SC_OK);

        return ((Deployments) HTTPRequest.build(requestEntity)
            .get()
            .getResponseEntity()
        ).getItems();
    }

    /**
     * Gets deployments response object to allow for usage of response
     *
     * @param userCredentials UserCredentials instance containing user details to use in api call
     * @return GetDeploymentsResponse instance
     */
    private Deployment getDeploymentByName(UserCredentials userCredentials, String deploymentName) {
        QueryParams filterMap = new QueryParams();
        filterMap.put("name[EQ]", deploymentName);
        List<Deployment> deploymentItems = getDeploymentItems(userCredentials, filterMap);
        return deploymentItems.stream().findFirst().orElseThrow(() -> new RuntimeException("Deployment not found"));
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return String - cloud context
     */
    public String getAuthTargetCloudContext(UserCredentials userCredentials) {
        String cloudContext = PropertiesContext.get("${customer}.${env}.customer_identity");

        String applicationNameFromConfig = getApplicationName();

        Deployment deploymentItem = getDeploymentByName(userCredentials, PropertiesContext.get("deployment"));

        Installation installationItem = deploymentItem.getInstallations()
            .stream()
            .filter(element -> element.getName().equals(PropertiesContext.get("installation_name")))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        Application applicationItem = installationItem.getApplications()
            .stream()
            .filter(element -> element.getServiceName().equalsIgnoreCase(applicationNameFromConfig))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        return cloudContext.concat(deploymentItem.getIdentity()).concat(installationItem.getIdentity()).concat(applicationItem.getIdentity());
    }

    private String getApplicationName() {
        try {
            return  PropertiesContext.get("application_name");
        } catch (IllegalArgumentException e) {
            return "cid";
        }
    }

    private static synchronized String getTokenSubjectForCustomer() {
        if (tokenSubject != null) {
            return tokenSubject;
        }

        try {
            // TODO : should be removed when AWS data will be available for staging too
            tokenSubject = PropertiesContext.get("${customer}.${env}.token_subject");
        } catch (IllegalArgumentException e) {
            tokenSubject = generateTokenSubject();
        }

        return tokenSubject;
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
     * Get Apriori customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     * @return filtered customer
     */
    public static Customer getApIntCustomerData() {
        return getCustomerData("ap-int");
    }

    /**
     * Get current customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     * @return filtered customer
     */
    public static Customer getCurrentCustomerData() {
        if (currentCustomer != null) {
            return currentCustomer;
        }
        return currentCustomer = getCustomerData(null);
    }

    /**
     * Get customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     * @param customerName - if null will return current customer data
     * @return
     */
    public static Customer getCustomerData(final String customerName) {
        String customerCloudReferenceName = getCustomerCloudReferenceName(customerName);

        RequestEntity customerRequest = RequestEntityUtil_Old.init(CustomersApiEnum.CUSTOMERS, Customers.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use("cloudReference[EQ]", customerCloudReferenceName));

        ResponseWrapper<Customers> customersResponseWrapper =  HTTPRequest.build(customerRequest).get();

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

    private static String getCustomerSiteIdByCustomer(Customer customerToProcess) {
        RequestEntity sitesRequest = RequestEntityUtil_Old.init(CustomersApiEnum.SITES_BY_CUSTOMER_ID, Sites.class)
            .inlineVariables(customerToProcess.getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Sites> sitesResponseWrapper =  HTTPRequest.build(sitesRequest).get();

        return sitesResponseWrapper.getResponseEntity()
            .getItems()
            .get(0)
            .getSiteId();
    }
}
