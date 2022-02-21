package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.apibase.services.common.objects.IdentityProviderResponse;
import com.apriori.cds.entity.response.IdentityProviderPagination;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
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
    private static String customerIdentity;
    private static String userIdentity;
    private static String idpIdentity;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static ResponseWrapper<Customer> customer;
    private static String userName;
    private static ResponseWrapper<User> user;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void cleanUp() {
        if (idpIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_SAML_BY_CUSTOMER_PROVIDER_IDS, customerIdentity, idpIdentity);
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5824", "5961"})
    @Description("Create an Identity provider for a customer")
    public void postCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> samlResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(samlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        idpIdentity = samlResponse.getResponseEntity().getIdentity();
    }

    @Test
    @TestRail(testCaseId = {"5963"})
    @Description("Update Identity provider for a customer")
    public void patchCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        idpIdentity = response.getResponseEntity().getIdentity();

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
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderResponse> idp = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SAML_BY_CUSTOMER_PROVIDER_IDS,
            IdentityProviderResponse.class,
            customerIdentity,
            idpIdentity
        );

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
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderPagination> idpPagination = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_SAML_BY_CUSTOMER_ID,
            IdentityProviderPagination.class,
            customerIdentity
        );

        assertThat(idpPagination.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(idpPagination.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @TestRail(testCaseId = {" "})
    @Description("Deletes identity provider")
    public void deleteCustomerIdentityProvider() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getIdentity();

        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(CDSAPIEnum.DELETE_SAML_BY_CUSTOMER_PROVIDER_IDS,
            customerIdentity,
            identityProviderIdentity
        );

        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
