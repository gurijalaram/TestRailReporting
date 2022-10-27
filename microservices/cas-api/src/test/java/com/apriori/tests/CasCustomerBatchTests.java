package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CustomerBatch;
import com.apriori.entity.response.CustomerBatches;
import com.apriori.entity.response.PostBatch;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CasCustomerBatchTests {
    private SoftAssertions soft = new SoftAssertions();
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
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
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        soft.assertThat(batch.getStatusCode())
            .isEqualTo(HttpStatus.SC_CREATED);
        soft.assertThat(batch.getResponseEntity().getCustomerIdentity())
            .isEqualTo(customerIdentity);

        ResponseWrapper<CustomerBatches> customerBatches = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_BATCHES, CustomerBatches.class)
            .token(token)
            .inlineVariables(customerIdentity)).get();

        soft.assertThat(customerBatches.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(customerBatches.getResponseEntity().getResponse().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        ResponseWrapper<String> deleteBatch = CasTestUtil.deleteBatch(customerIdentity, batchIdentity);

        soft.assertThat(deleteBatch.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);
        soft.assertAll();
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

        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerBatch> customerBatch = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.GET_BATCH, CustomerBatch.class)
            .token(token)
            .inlineVariables(customerIdentity, batchIdentity)).get();

        soft.assertThat(customerBatch.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(customerBatch.getResponseEntity().getIdentity())
            .isEqualTo(batchIdentity);

        ResponseWrapper<String> deleteBatch = CasTestUtil.deleteBatch(customerIdentity, batchIdentity);

        soft.assertThat(deleteBatch.getStatusCode())
            .isEqualTo(HttpStatus.SC_NO_CONTENT);
        soft.assertAll();
    }
}
