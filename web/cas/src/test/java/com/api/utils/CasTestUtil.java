package com.api.utils;

import com.apriori.apibase.services.cas.objects.Customer;
import com.apriori.apibase.services.cas.objects.SingleCustomerResponse;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.Arrays;

public class CasTestUtil extends TestUtil {

    /**
     * POST call to add a customer
     *
     * @param url            - the endpoint
     * @param klass          - the response class
     * @param token          - the token
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param description    - the description
     * @param email          - the email pattern
     * @return ResponseWrapper <SingleCustomerResponse>
     */
    public ResponseWrapper<SingleCustomerResponse> addCustomer(String url, Class klass, String token, String name, String cloudReference, String description, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setBody("customer",
                        new Customer().setName(name)
                                .setCloudReference(cloudReference)
                                .setDescription(description)
                                .setCustomerType("CLOUD_ONLY")
                                .setActive(true)
                                .setMfaRequired(false)
                                .setUseExternalIdentityProvider(false)
                                .setMaxCadFileRetentionDays(584)
                                .setEmailDomains(Arrays.asList(email + ".com", "gmail.com")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * PATCH call to update a customer
     *
     * @param url            - the endpoint
     * @param klass          - the response class
     * @param token          - the token
     * @param email          - the email pattern
     * @return ResponseWrapper <SingleCustomerResponse>
     */
    public ResponseWrapper<SingleCustomerResponse> updateCustomer(String url, Class klass, String token, String email) {
        RequestEntity requestEntity = RequestEntity.init(url,klass)
                .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
                .setBody("customer",
                        new Customer().setEmailDomains(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }
}