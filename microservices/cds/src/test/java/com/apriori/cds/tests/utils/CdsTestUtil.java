package com.apriori.cds.tests.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.entity.response.Customer;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.Arrays;

public class CdsTestUtil extends TestUtil {

    protected <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.get(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaApi()
        );
    }

    /**
     * POST call to add a customer
     *
     * @param url          - the endpoint
     * @param klass        - the response class
     * @param name         - the customer name
     * @param salesForceId - the sales force id
     * @param email        - the email pattern
     * @return ResponseWrapper<Customer>
     */
    public ResponseWrapper<Customer> addCustomer(String url, Class klass, String name, String salesForceId, String email) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("customer",
                new Customer().setName(name)
                    .setDescription("Add new customers api test")
                    .setCustomerType("CLOUD_ONLY")
                    .setCreatedBy("#SYSTEM00000")
                    .setSalesforceId(salesForceId)
                    .setActive(true)
                    .setMfaRequired(false)
                    .setUseExternalIdentityProvider(false)
                    .setMaxCadFileRetentionDays(1095)
                    .setEmailRegexPatterns(Arrays.asList(email + ".com", email + ".co.uk")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }
}
