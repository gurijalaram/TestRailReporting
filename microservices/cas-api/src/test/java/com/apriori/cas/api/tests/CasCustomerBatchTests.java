package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.CustomerBatch;
import com.apriori.cas.api.models.response.CustomerBatches;
import com.apriori.cas.api.models.response.PostBatch;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class CasCustomerBatchTests {
    private CasTestUtil casTestUtil;
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String customerIdentity;
    private CdsTestUtil cdsTestUtil;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser()
            .useTokenInRequests();
        casTestUtil = new CasTestUtil(requestEntityUtil);
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
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
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

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

        casTestUtil.deleteBatch(customerIdentity, batchIdentity);
    }

    @Test
    @TestRail(id = {5668, 5670, 5675})
    @Description("Upload a new user batch file, Get the Batch identified by its identity, Delete the Batch by its identity.")
    public void getBatchById() {
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerBatch> customerBatch = casTestUtil.getCommonRequest(CASAPIEnum.BATCH,
            CustomerBatch.class,
            HttpStatus.SC_OK,
            customerIdentity,
            batchIdentity);

        soft.assertThat(customerBatch.getResponseEntity().getIdentity())
            .isEqualTo(batchIdentity);
        soft.assertAll();

        casTestUtil.deleteBatch(customerIdentity, batchIdentity);
    }
}
