package com.apriori.cds.tests.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.entity.response.Customer;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.Arrays;

public class CdsTestUtil extends TestUtil {

    protected <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.get(
            RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
            new RequestAreaApi()
        );
    }

    // TODO: 11/02/2021 ciene - all methods below to be moved into a util class

    /**
     * POST call to add a customer
     *
     * @param url            - the endpoint
     * @param klass          - the response class
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param salesForceId   - the sales force id
     * @param email          - the email pattern
     * @return ResponseWrapper<Customer>
     */
    public ResponseWrapper<Customer> addCustomer(String url, Class klass, String name, String cloudReference, String salesForceId, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("customer",
                new Customer().setName(name)
                    .setDescription("Add new customers api test")
                    .setCustomerType("CLOUD_ONLY")
                    .setCreatedBy("#SYSTEM00000")
                    .setCloudReference(cloudReference)
                    .setSalesforceId(salesForceId)
                    .setActive(true)
                    .setMfaRequired(false)
                    .setUseExternalIdentityProvider(false)
                    .setMaxCadFileRetentionDays(1095)
                    .setEmailRegexPatterns(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }

    /**
     * Delete an api customer
     *
     * @param deleteCustomerEndpoint - the endpoint to delete a customer
     */
    public void deleteCustomer(String deleteCustomerEndpoint) {
        RequestEntity requestEntity = RequestEntity.init(deleteCustomerEndpoint, null)
            .setHeaders("Content-Type", "application/json");

        ResponseWrapper<String> deleteCustomer = GenericRequestUtil.delete(requestEntity, new RequestAreaApi());

        assertThat(deleteCustomer.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }

    /**
     * Delete an api user
     *
     * @param deleteUserEndpoint - the endpoint to delete a user
     */
    public void deleteUser(String deleteUserEndpoint) {
        RequestEntity requestEntity = RequestEntity.init(deleteUserEndpoint, null)
            .setHeaders("Content-Type", "application/json");

        ResponseWrapper<String> deleteUser = GenericRequestUtil.delete(requestEntity, new RequestAreaApi());

        assertThat(deleteUser.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
