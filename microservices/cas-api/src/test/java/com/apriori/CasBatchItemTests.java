package com.apriori;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.BatchItem;
import com.apriori.entity.response.BatchItems;
import com.apriori.entity.response.Customer;
import com.apriori.entity.response.PostBatch;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CasBatchItemTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @After
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5671})
    @Description("Returns a list of batch items for the customer batch.")
    public void getBatchItems() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<BatchItems> getItems = casTestUtil.getCommonRequest(CASAPIEnum.BATCH_ITEMS,
            BatchItems.class,
            HttpStatus.SC_OK,
            customerIdentity,
            batchIdentity);

        soft.assertThat(getItems.getResponseEntity().getPageItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5672})
    @Description("Creates users from Batch by provided identities.")
    public void createBatchItems() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        CasTestUtil.newUsersFromBatch(customerIdentity, batchIdentity);

    }

    @Test
    @TestRail(id = {5674})
    @Description("Get the Batch Item identified by its identity.")
    public void getItemById() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);

        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = CasTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<BatchItems> getItems = casTestUtil.getCommonRequest(CASAPIEnum.BATCH_ITEMS,
            BatchItems.class,
            HttpStatus.SC_OK,
            customerIdentity,
            batchIdentity);

        BatchItem batchItem = getItems.getResponseEntity().getItems().get(0);
        String itemId = batchItem.getIdentity();
        String userName = batchItem.getUserName();

        ResponseWrapper<BatchItem> getItem = casTestUtil.getCommonRequest(CASAPIEnum.BATCH_ITEM,
            BatchItem.class,
            HttpStatus.SC_OK,
            customerIdentity,
            batchIdentity,
            itemId);

        soft.assertThat(getItem.getResponseEntity().getIdentity())
            .isEqualTo(itemId);
        soft.assertThat(getItem.getResponseEntity().getUserName())
            .isEqualTo(userName);
    }

    // TODO endpoint is not implemented
    @Ignore("Endpoint is not implemented")
    @Test
    @TestRail(id = {5673})
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

        ResponseWrapper<BatchItems> getItems = casTestUtil.getCommonRequest(CASAPIEnum.BATCH_ITEMS,
            BatchItems.class,
            HttpStatus.SC_OK,
            customerIdentity,
            batchIdentity);

        String itemId = getItems.getResponseEntity().getItems().get(0).getIdentity();

        CasTestUtil.updateBatchItem(customerIdentity, batchIdentity, itemId);
    }
}
