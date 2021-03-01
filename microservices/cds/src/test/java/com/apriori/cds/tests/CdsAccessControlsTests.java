package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.objects.response.AccessControl;
import com.apriori.cds.objects.response.AccessControls;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsAccessControlsTests extends CdsTestUtil {
    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private String accessControlIdentityEndpoint;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (accessControlIdentityEndpoint != null) {
            delete(accessControlIdentityEndpoint);
        }
        if (userIdentityEndpoint != null) {
            delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "3289")
    @Description("API returns a list of all the access controls in the CDS DB")
    public void getAccessControls() {
        url = String.format(url, "access-controls");
        ResponseWrapper<AccessControls> response = getCommonRequest(url, true, AccessControls.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getOutOfContext(), is(notNullValue()));
    }

    @Test
    @TestRail(testCaseId = "")
    @Description("Adding out of context access control")
    public void postAccessControl() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String usersEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users")));

        ResponseWrapper<User> user = addUser(usersEndpoint, User.class, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/users/".concat(userIdentity))));

        String accessControlEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat(String.format("/users/%s/access-controls", userIdentity))));
        ResponseWrapper<AccessControl> accessControlResponse = addAccessControl(accessControlEndpoint, AccessControl.class);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getResponse().getIdentity();

        accessControlIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s/access-controls/%s", customerIdentity, userIdentity, accessControlIdentity));

        assertThat(accessControlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(accessControlResponse.getResponseEntity().getResponse().getOutOfContext(), is(true));
    }
}
