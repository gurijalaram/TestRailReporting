package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.CustomerBatch;
import com.apriori.entity.response.CustomerBatches;
import com.apriori.entity.response.PostBatch;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasCustomerBatchTests {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5668", "5669", "5675"})
    @Description("Upload a new user batch file, Returns a list of batches for the customer, Delete the Batch by its identity.")
    public void getCustomerBatches() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        assertThat(batch.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch.getResponseEntity().getCustomerIdentity(), is(equalTo(customerIdentity)));

        ResponseWrapper<CustomerBatches> customerBatches = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.BATCH_ITEM, true, CustomerBatches.class,
                    new APIAuthentication().initAuthorizationHeaderContent(token))
                .inlineVariables("batches"))
            .get();

        assertThat(customerBatches.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerBatches.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        ResponseWrapper<String> deleteBatch = CasTestUtil.deleteBatch(customerIdentity, batchIdentity);

        assertThat(deleteBatch.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }

    @Test
    @TestRail(testCaseId = {"5668", "5670", "5675"})
    @Description("Upload a new user batch file, Get the Batch identified by its identity, Delete the Batch by its identity.")
    public void getBatchById() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerBatch> customerBatch = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_BATCHES, true, CustomerBatch.class,
            new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables(customerIdentity, "batches", batchIdentity)).get();

        assertThat(customerBatch.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerBatch.getResponseEntity().getIdentity(), is(equalTo(batchIdentity)));

        ResponseWrapper<String> deleteBatch = CasTestUtil.deleteBatch(customerIdentity, batchIdentity);

        assertThat(deleteBatch.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
