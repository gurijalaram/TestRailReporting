package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItems;
import com.apriori.entity.response.PostBatch;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.service.HTTP2Request;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CasBatchItemTests {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken();
    }

    @Test
    @TestRail(testCaseId = {"5671"})
    @Description("Returns a list of batch items for the customer batch.")
    public void getBatchItems() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<BatchItems> getItems = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.GET_BATCHES, true, BatchItems.class,
                new APIAuthentication().initAuthorizationHeaderContent(token))
            .inlineVariables(customerIdentity, "batches", batchIdentity, "items")).get();

        assertThat(getItems.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getItems.getResponseEntity().getPageItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5672"})
    @Description("Creates users from Batch by provided identities.")
    public void createBatchItems() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<String> batchItems = CasTestUtil.newUsersFromBatch(customerIdentity, batchIdentity);

        assertThat(batchItems.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }

    @Test
    @TestRail(testCaseId = {"5674"})
    @Description("Get the Batch Item identified by its identity.")
    public void getItemById() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<BatchItems> getItems = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.GET_BATCHES, true, BatchItems.class,
                    new APIAuthentication().initAuthorizationHeaderContent(token))
                .inlineVariables(customerIdentity, "batches", batchIdentity, "items"))
            .get();

        BatchItem batchItem = getItems.getResponseEntity().getItems().get(0);
        String itemId = batchItem.getIdentity();
        String userName = batchItem.getUserName();

        ResponseWrapper<BatchItem> getItem = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.BATCH_ITEM, true, BatchItem.class,
                    new APIAuthentication().initAuthorizationHeaderContent(token))
                .inlineVariables(customerIdentity, "batches", batchIdentity, "items", itemId))
            .get();

        assertThat(getItem.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getItem.getResponseEntity().getIdentity(), is(equalTo(itemId)));
        assertThat(getItem.getResponseEntity().getUserName(), is(equalTo(userName)));
    }

    // TODO endpoint is not implemented
    @Ignore("Endpoint is not implemented")
    @Test
    @TestRail(testCaseId = {"5673"})
    @Description("Update an existing Batch Item identified by its identity.")
    public void updateBatchItem() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<BatchItems> getItems = HTTP2Request.build(CasTestUtil.getCommonRequest(CASAPIEnum.GET_BATCHES, true, BatchItems.class,
                    new APIAuthentication().initAuthorizationHeaderContent(token))
                .inlineVariables(customerIdentity, "batches", batchIdentity, "items"))
            .get();

        String itemId = getItems.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<BatchItem> updateItem = CasTestUtil.updateBatchItem(customerIdentity, batchIdentity, itemId);

        assertThat(updateItem.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
