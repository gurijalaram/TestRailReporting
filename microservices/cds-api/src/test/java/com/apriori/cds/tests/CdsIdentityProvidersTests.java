package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.cds.entity.response.IdentityProviderPagination;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsIdentityProvidersTests {
    private static String url;
    private static String customerIdentityEndpoint;
    private static String userIdentityEndpoint;
    private static String idpIdentityEndpoint;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static ResponseWrapper<Customer> customer;
    private static String userName;
    private static ResponseWrapper<User> user;
    private static String userIdentity;

    @BeforeClass
    public static void setDetails() {
        url = Constants.getServiceUrl();

        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
        userIdentityEndpoint = String.format(url, String.format("customers/%s/users/%s", customerIdentity, userIdentity));
    }

    @AfterClass
    public static void cleanUp() {
        if (idpIdentityEndpoint != null) {
            cdsTestUtil.delete(idpIdentityEndpoint);
        }
        if (userIdentityEndpoint != null) {
            cdsTestUtil.delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }
    }

    @Test
    @TestRail(testCaseId = {"5824", "5961"})
    @Description("Create an Identity provider for a customer")
    public void postCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> samlResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(samlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        String idpIdentity = samlResponse.getResponseEntity().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));
    }

    @Test
    @TestRail(testCaseId = {"5963"})
    @Description("Update Identity provider for a customer")
    public void patchCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        ResponseWrapper<IdentityProviderResponse> updatedDescription = cdsTestUtil.patchIdp(customerIdentity, idpIdentity, userIdentity);
        assertThat(updatedDescription.getResponseEntity().getDescription(), is(equalTo("patch IDP using Automation")));
    }

    @Test
    @TestRail(testCaseId = {"5962"})
    @Description("getIDP details by Identity")
    public void getIDPbyIdentity() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        ResponseWrapper<IdentityProviderResponse> idp = cdsTestUtil.getCommonRequest(idpIdentityEndpoint, IdentityProviderResponse.class);

        assertThat(idp.getStatusCode(), CoreMatchers.is(CoreMatchers.equalTo(HttpStatus.SC_OK)));
        assertThat(idp.getResponseEntity().getIdentity(), is(equalTo(idpIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5960"})
    @Description("getIDP list for customer")
    public void getListOfIDPsForCustomer() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String idpIdentity = response.getResponseEntity().getIdentity();
        idpIdentityEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, idpIdentity));

        String identityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers", customerIdentity));
        ResponseWrapper<IdentityProviderPagination> idpPagination = cdsTestUtil.getCommonRequest(identityProviderEndpoint, IdentityProviderPagination.class);

        assertThat(idpPagination.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(idpPagination.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {" "})
    @Description("Deletes identity provider")
    public void deleteCustomerIdentityProvider() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getIdentity();

        String deleteIdentityProviderEndpoint = String.format(url, String.format("customers/%s/identity-providers/%s", customerIdentity, identityProviderIdentity));
        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(deleteIdentityProviderEndpoint);

        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
