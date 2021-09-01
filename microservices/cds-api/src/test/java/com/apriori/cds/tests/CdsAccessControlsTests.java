package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.objects.response.AccessControlResponse;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsAccessControlsTests  {
    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String accessControlIdentityEndpoint;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (accessControlIdentityEndpoint != null) {
            cdsTestUtil.delete(accessControlIdentityEndpoint);
        }
        if (userIdentityEndpoint != null) {
            cdsTestUtil.delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"3294"})
    @Issue("MIC-1972")
    @Description("Adding out of context access control")
    public void postAccessControl() {

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getResponse().getIdentity();

        accessControlIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s/access-controls/%s", customerIdentity, userIdentity, accessControlIdentity));

        assertThat(accessControlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(accessControlResponse.getResponseEntity().getResponse().getOutOfContext(), is(true));
    }
}
