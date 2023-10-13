package com.apriori.models;

import com.apriori.enums.CustomersApiEnum;
import com.apriori.enums.DeploymentsAPIEnum;
import com.apriori.enums.TokenEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.request.TokenRequest;
import com.apriori.models.response.Application;
import com.apriori.models.response.Claims;
import com.apriori.models.response.Customer;
import com.apriori.models.response.Customers;
import com.apriori.models.response.Deployment;
import com.apriori.models.response.Deployments;
import com.apriori.models.response.Installation;
import com.apriori.models.response.Sites;
import com.apriori.models.response.Token;
import com.apriori.models.response.TokenInformation;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;

import java.util.List;
import java.util.Objects;
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

        RequestEntity requestEntity = RequestEntityUtil.init(TokenEnum.POST_TOKEN, Token.class)
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
        final RequestEntity requestEntity = RequestEntityUtil
            .init(DeploymentsAPIEnum.DEPLOYMENTS, Deployments.class)
            .token(userCredentials.getToken())
            .inlineVariables(
                PropertiesContext.get("customer_identity")
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
        String cloudContext = PropertiesContext.get("customer_identity");

        String applicationNameFromConfig = PropertiesContext.get("application_name");

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

    private static synchronized String getTokenSubjectForCustomer() {
        if (tokenSubject != null) {
            return tokenSubject;
        }

        try {
            // TODO : should be removed when AWS data will be available for staging too
            tokenSubject = PropertiesContext.get("${customer}.${customer_aws_account_type}.token_subject");
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
     * Get current customer data
     * By a cloud reference name filter all customers to find a user used for the environment
     * @return filtered customer
     */
    public static Customer getCurrentCustomerData() {
        if (currentCustomer != null) {
            return currentCustomer;
        }

        RequestEntity customerRequest = RequestEntityUtil.init(CustomersApiEnum.CUSTOMERS, Customers.class)
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Customers> customersResponseWrapper =  HTTPRequest.build(customerRequest).get();

        return currentCustomer = customersResponseWrapper.getResponseEntity().getItems()
            .stream()
            .filter(customer -> Objects.nonNull(customer.getCloudReference()))
            .filter(customer -> customer.getCloudReference()
                .equals(PropertiesContext.get("${customer}.cloud_reference_name")
                )
            )
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private static String getCustomerSiteIdByCustomer(Customer customerToProcess) {
        RequestEntity sitesRequest = RequestEntityUtil.init(CustomersApiEnum.SITES_BY_CUSTOMER_ID, Sites.class)
            .inlineVariables(customerToProcess.getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Sites> sitesResponseWrapper =  HTTPRequest.build(sitesRequest).get();

        return sitesResponseWrapper.getResponseEntity()
            .getItems()
            .get(0)
            .getSiteId();
    }
}
