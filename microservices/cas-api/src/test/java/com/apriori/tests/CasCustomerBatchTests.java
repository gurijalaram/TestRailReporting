package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.CustomerBatch;
import com.apriori.entity.response.CustomerBatches;
import com.apriori.entity.response.PostBatch;
import com.apriori.tests.utils.CasTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CasCustomerBatchTests extends TestUtil {
    private String token;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CasTestUtil casTestUtil = new CasTestUtil();
    private String url = CASAPIEnum.GET_CUSTOMERS.getEndpointString();

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

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();

        String batchEndpoint = url + customerIdentity + "/batches/";

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getResponse().getIdentity();

        assertThat(batch.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(batch.getResponseEntity().getResponse().getCustomerIdentity(), is(equalTo(customerIdentity)));

        ResponseWrapper<CustomerBatches> customerBatches = new CommonRequestUtil().getCommonRequest(batchEndpoint, true, CustomerBatches.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(customerBatches.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerBatches.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        ResponseWrapper deleteBatch = casTestUtil.deleteBatch(customerIdentity, batchIdentity);

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

        ResponseWrapper<Customer> customer = casTestUtil.addCustomer(customerName, cloudRef, description, email);

        String customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<PostBatch> batch = casTestUtil.addBatchFile(customerIdentity);

        String batchIdentity = batch.getResponseEntity().getResponse().getIdentity();
        String batchUrl = url + customerIdentity + "/batches/" + batchIdentity;

        ResponseWrapper<CustomerBatch> customerBatch = new CommonRequestUtil().getCommonRequest(batchUrl, true, CustomerBatch.class,
                new APIAuthentication().initAuthorizationHeaderContent(token));

        assertThat(customerBatch.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customerBatch.getResponseEntity().getResponse().getIdentity(), is(equalTo(batchIdentity)));

        ResponseWrapper deleteBatch = casTestUtil.deleteBatch(customerIdentity, batchIdentity);

        assertThat(deleteBatch.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
