package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.CustomerBatch;
import com.apriori.cas.models.response.CustomerBatches;
import com.apriori.cas.models.response.PostBatch;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasCustomerBatchTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @AfterEach
    public void cleanUp() {
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5668, 5669, 5675})
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

        soft.assertThat(batch.getResponseEntity().getCustomerIdentity())
            .isEqualTo(customerIdentity);

        ResponseWrapper<CustomerBatches> customerBatches = casTestUtil.getCommonRequest(CASAPIEnum.BATCHES,
            CustomerBatches.class,
            HttpStatus.SC_OK,
            customerIdentity);

        soft.assertThat(customerBatches.getResponseEntity().getResponse().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();

        CasTestUtil.deleteBatch(customerIdentity, batchIdentity);
    }

    @Test
    @TestRail(id = {5668, 5670, 5675})
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

        ResponseWrapper<CustomerBatch> customerBatch = casTestUtil.getCommonRequest(CASAPIEnum.BATCH,
            CustomerBatch.class,
            HttpStatus.SC_OK,
            customerIdentity,
            batchIdentity);

        soft.assertThat(customerBatch.getResponseEntity().getIdentity())
            .isEqualTo(batchIdentity);
        soft.assertAll();

        CasTestUtil.deleteBatch(customerIdentity, batchIdentity);
    }
}