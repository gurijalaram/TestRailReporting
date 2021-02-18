package com.apriori.tests.utils;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.Site;
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
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> addCustomer(String url, Class klass, String token, String name, String cloudReference, String description, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("customer",
                new Customer().setName(name)
                    .setCloudReference(cloudReference)
                    .setDescription(description)
                    .setCustomerType("CLOUD_ONLY")
                    .setActive(true)
                    .setMfaRequired(true)
                    .setUseExternalIdentityProvider(false)
                    .setMaxCadFileRetentionDays(584)
                    .setEmailDomains(Arrays.asList(email + ".com", "gmail.com")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * PATCH call to update a customer
     *
     * @param url   - the endpoint
     * @param klass - the response class
     * @param token - the token
     * @param email - the email pattern
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> updateCustomer(String url, Class klass, String token, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("customer",
                new Customer().setEmailDomains(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url - the endpoint
     * @param token - token
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> resetMfa(String url, String token) {
        RequestEntity requestEntity = RequestEntity.init(url, null)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * @param url - the endpoint
     * @param klass - the response class
     * @param token - token
     * @param siteId - site ID
     * @return <T>ResponseWrapper <T>
     */
    public <T> ResponseWrapper<T> validateSite(String url, Class klass, String token, String siteId) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setBody("site",
                new Site().setSiteId(siteId));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }
}