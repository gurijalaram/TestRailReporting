package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.common.objects.IdentityProviderRequest;
import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.cds.entity.response.IdentityProviderPagination;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsIdentityProvidersTests extends CdsTestUtil {
    private String url;
    private String customerIdentityEndpoint;
    private String userIdentityEndpoint;
    private String idpIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (idpIdentityEndpoint != null) {
            delete(idpIdentityEndpoint);
        }
        if (userIdentityEndpoint != null) {
            delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"5824", "5961"})
    @Description("Create an Identity provider for a customer")
    public void postCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));
        ResponseWrapper<IdentityProviderResponse> response = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getResponse().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));
    }

    @Test
    @TestRail(testCaseId = "5963")
    @Description("Update Identity provider for a customer")
    public void patchCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));
        ResponseWrapper<IdentityProviderResponse> response = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getResponse().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        RequestEntity requestEntity = RequestEntity.init(idpIdentityEndpoint, IdentityProviderResponse.class)
            .setHeaders("Content-Type", "application/json")
            .setBody("identityProvider",
                new IdentityProviderRequest().setDescription("patch IDP using Automation")
                    .setContact(userIdentity));
        ResponseWrapper<IdentityProviderResponse> updatedDescription = GenericRequestUtil.patch(requestEntity, new RequestAreaApi());
        assertThat(updatedDescription.getResponseEntity().getResponse().getDescription(), is(equalTo("patch IDP using Automation")));
    }

    @Test
    @TestRail(testCaseId = "5962")
    @Description("getIDP details by Identity")
    public void getIDPbyIdentity() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));
        ResponseWrapper<IdentityProviderResponse> response = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getResponse().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        ResponseWrapper<IdentityProviderResponse> idp = getCommonRequest(idpIdentityEndpoint, true, IdentityProviderResponse.class);

        assertThat(idp.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(idp.getResponseEntity().getResponse().getIdentity(), is(equalTo(idpIdentity)));
    }

    @Test
    @TestRail(testCaseId = "5960")
    @Description("getIDP list for customer")
    public void getListOfIDPsForCustomer() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));
        ResponseWrapper<IdentityProviderResponse> response = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getResponse().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        ResponseWrapper<IdentityProviderPagination> idp = getCommonRequest(identityProviderEndpoint, true, IdentityProviderPagination.class);

        assertThat(idp.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(idp.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = " ")
    @Description("Deletes identity provider")
    public void deleteCustomerIdentityProvider() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        ResponseWrapper<User> user = addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getResponse().getIdentity();

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = addSaml(identityProviderEndpoint, IdentityProviderResponse.class, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getResponse().getIdentity();

        String deleteIdentityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, identityProviderIdentity));
        ResponseWrapper<String> deleteResponse = delete(deleteIdentityProviderEndpoint);

        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
