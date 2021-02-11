package com.apriori.cds.tests.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.entity.response.User;
import com.apriori.cds.entity.response.UserProfile;
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

    // TODO: 10/02/2021 ciene - to be moved to a more generic utility class.  possibly pass create an addcustomer class with a constructor create the customer

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
     * POST call to add a customer
     *
     * @param url          - the endpoint
     * @param klass        - the response class
     * @param userName     - the user name
     * @param customerName - the customer name
     * @return ResponseWrapper<User>
     */
    public ResponseWrapper<User> addUser(String url, Class klass, String userName, String customerName) {
        RequestEntity requestEntity = RequestEntity.init(url, klass)
            .setHeaders("Content-Type", "application/json")
            .setBody("user",
                new User().setUsername(userName)
                    .setEmail(userName.concat("@").concat(customerName).concat(".com"))
                    .setCreatedBy("#SYSTEM00000")
                    .setActive(true)
                    .setUserType("AP_CLOUD_USER")
                    .setUserProfile(new UserProfile().setGivenName(userName)
                        .setFamilyName("Automater")
                        .setJobTitle("Automation Engineer")
                        .setDepartment("Automation")
                        .setSupervisor("Ciene Frith")
                        .setCreatedBy("#SYSTEM00000")));

        return GenericRequestUtil.post(requestEntity, new RequestAreaApi());
    }
}
