package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.CASCustomerEnum;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.CASCustomerRequest;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;

import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;

public class CustomerUtil {
    private RequestEntityUtil requestEntityUtil;

    public CustomerUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * POST call to add a customer
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param salesForceId   - the sales force id
     * @param email          - the email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> addCustomer(
        String name,
        String customerType,
        String cloudReference,
        String salesForceId,
        String email) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMERS, Customer.class)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "customer",
                Customer.builder().name(name)
                    .description("Add new customers api test")
                    .customerType(customerType)
                    .createdBy("#SYSTEM00000")
                    .cloudReference(cloudReference)
                    .salesforceId(salesForceId)
                    .active(true)
                    .mfaRequired(false)
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(1095)
                    .maxCadFileSize(51)
                    .emailRegexPatterns(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Add a customer with random data
     *
     * @param rcd random customer data
     * @return new object
     */
    public ResponseWrapper<Customer> addCustomer(RandomCustomerData rcd) {

        return addCustomer(rcd.getCustomerName(), rcd.getCustomerType(), rcd.getCloudRef(), rcd.getSalesForceId(), rcd.getEmailPattern());
    }

    /**
     * Creates customer via CAS API with service accounts
     *
     * @param name           - the customer name
     * @param cloudReference - the cloud reference name
     * @param email          - the email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> addCASCustomer(
        String name,
        String cloudReference,
        String email) {

        RequestEntity requestEntity = requestEntityUtil.init(CASCustomerEnum.CUSTOMERS, Customer.class)
            .body(
                "customer",
                CASCustomerRequest.builder().name(name)
                    .cloudReference(cloudReference)
                    .description("Add new customers api test")
                    .salesforceId(new GenerateStringUtil().generateNumericString("SFID", 10))
                    .customerType("CLOUD_ONLY")
                    .active(true)
                    .mfaRequired(true)
                    .mfaAuthenticator("ONE_TIME_PASSWORD")
                    .useExternalIdentityProvider(false)
                    .maxCadFileRetentionDays(584)
                    .maxCadFileSize(51)
                    .emailDomains(Arrays.asList(email + ".com", email + ".co.uk"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates customer by customer id
     *
     * @param customerIdentity    - customer identity
     * @param updatedEmailPattern - new value of email pattern
     * @return new object
     */
    public ResponseWrapper<Customer> updateCustomer(String customerIdentity, String updatedEmailPattern) {
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMER_BY_ID, Customer.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .headers(new HashMap<>() {
                {
                    put("Content-Type", "application/json");
                }
            })
            .body(
                "customer",
                Customer.builder()
                    .emailRegexPatterns(Arrays.asList(updatedEmailPattern + ".com", updatedEmailPattern + ".co.uk"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }
}
