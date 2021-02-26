package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
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

public class CdsIdentityProvidersTests extends CdsTestUtil {
    private String url;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = "5314")
    @Description("Get a list of deployments for a customer")
    public void postCustomerIdentityProviders() {
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

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));

        ResponseWrapper<IdentityProviderResponse> response = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, userName);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }

    @Test
    @TestRail(testCaseId = " ")
    @Description("Deletes identity provider")
    public void deleteCustomerIdentityProvider() {
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

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getResponse().getIdentity();

        String deleteIdentityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, identityProviderIdentity));
        ResponseWrapper<String> deleteResponse = delete(deleteIdentityProviderEndpoint);

        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
        assertThat(deleteResponse.getResponseEntity(), is(emptyString()));
    }
}
