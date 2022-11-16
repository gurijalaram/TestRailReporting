package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.IdentityProviderPagination;
import com.apriori.cds.entity.response.IdentityProviderResponse;
import com.apriori.cds.enums.CDSAPIEnum;
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
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsIdentityProvidersTests {
    private String customerIdentity;
    private String userIdentity;
    private String idpIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private ResponseWrapper<Customer> customer;
    private String userName;
    private ResponseWrapper<User> user;

    @Before
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @After
    public void cleanUp() {
        if (idpIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS, customerIdentity, idpIdentity);
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Issue("IDS-851")
    @TestRail(testCaseId = {"5824", "5961"})
    @Description("Create an Identity provider for a customer")
    public void postCustomerIdentityProviders() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> samlResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(samlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        idpIdentity = samlResponse.getResponseEntity().getIdentity();
    }

    @Test
    @Issue("IDS-851")
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
    @Issue("IDS-851")
    @TestRail(testCaseId = {"5962"})
    @Description("getIDP details by Identity")
    public void getIDPbyIdentity() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderResponse> idp = cdsTestUtil.getCommonRequest(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS,
            IdentityProviderResponse.class,
            HttpStatus.SC_OK,
            customerIdentity,
            idpIdentity
        );

        assertThat(idp.getResponseEntity().getIdentity(), is(equalTo(idpIdentity)));
    }

    @Test
    @Issue("IDS-851")
    @TestRail(testCaseId = {"5960"})
    @Description("getIDP list for customer")
    public void getListOfIDPsForCustomer() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> response = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        idpIdentity = response.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviderPagination> idpPagination = cdsTestUtil.getCommonRequest(CDSAPIEnum.SAML_BY_CUSTOMER_ID,
            IdentityProviderPagination.class,
            HttpStatus.SC_OK,
            customerIdentity
        );

        assertThat(idpPagination.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    @Issue("IDS-851")
    @TestRail(testCaseId = {" "})
    @Description("Deletes identity provider")
    public void deleteCustomerIdentityProvider() {
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<IdentityProviderResponse> identityProviderResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, userName);
        String identityProviderIdentity = identityProviderResponse.getResponseEntity().getIdentity();

        ResponseWrapper<String> deleteResponse = cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS,
            customerIdentity,
            identityProviderIdentity
        );

        assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}
