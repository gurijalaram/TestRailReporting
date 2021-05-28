package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItems;
import com.apriori.entity.response.PostBatch;
import com.apriori.entity.response.SingleCustomer;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CasBatchItemTests extends TestUtil {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CasTestUtil casTestUtil = new CasTestUtil();
    private String url = String.format(Constants.getApiUrl(), "customers/");

    @Before
    public void getToken() {
        token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCasServiceHost(),
                HttpStatus.SC_CREATED,
                Constants.getCasTokenUsername(),
                Constants.getCasTokenEmail(),
                Constants.getCasTokenIssuer(),
                Constants.getCasTokenSubject());
    }

    @Test
    @TestRail(testCaseId = {"5671"})
    @Description("Returns a list of batch items for the customer batch.")
    public void getBatchItems() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<SingleCustomer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getResponse().getIdentity();
        String itemsUrl = url + customerIdentity + "/batches/" + batchIdentity + "/items/";

        ResponseWrapper<BatchItems> getItems = new CommonRequestUtil().getCommonRequest(itemsUrl, true, BatchItems.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(getItems.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getItems.getResponseEntity().getResponse().getPageItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5672"})
    @Description("Creates users from Batch by provided identities.")
    public void createBatchItems() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<SingleCustomer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper batchItems = casTestUtil.newUsersFromBatch(customerIdentity, batchIdentity);

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

        ResponseWrapper<SingleCustomer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getResponse().getIdentity();
        String itemsUrl = url + customerIdentity + "/batches/" + batchIdentity + "/items/";

        ResponseWrapper<BatchItems> getItems = new CommonRequestUtil().getCommonRequest(itemsUrl, true, BatchItems.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        BatchItem batchItem = getItems.getResponseEntity().getResponse().getItems().get(0);
        String itemId = batchItem.getIdentity();
        String userName = batchItem.getUserName();
        String itemUrl = itemsUrl + itemId;

        ResponseWrapper<BatchItem> getItem = new CommonRequestUtil().getCommonRequest(itemUrl, true, BatchItem.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(getItem.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(getItem.getResponseEntity().getResponse().getIdentity(), is(equalTo(itemId)));
        assertThat(getItem.getResponseEntity().getResponse().getUserName(), is(equalTo(userName)));
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

        ResponseWrapper<SingleCustomer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getResponse().getIdentity();
        String itemsUrl = url + customerIdentity + "/batches/" + batchIdentity + "/items/";

        ResponseWrapper<BatchItems> getItems = new CommonRequestUtil().getCommonRequest(itemsUrl, true, BatchItems.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        String itemId = getItems.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<BatchItem> updateItem = casTestUtil.updateBatchItem(customerIdentity, batchIdentity, itemId);

        assertThat(updateItem.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
