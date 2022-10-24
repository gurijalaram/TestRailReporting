package com.apriori.tests;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.entity.response.IdentityProviderResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.IdentityProvider;
import com.apriori.entity.response.IdentityProviders;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CasIdentityProvidersTests extends TestUtil {

    private SoftAssertions soft = new SoftAssertions();
    private String token;
    private String customerIdentity;
    private String userIdentity;
    private String idpIdentity;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerName;
    private String cloudRef;
    private String email;
    private String description;
    private String userName;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        description = customerName + " Description";
        email = customerName.toLowerCase();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);
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
    @TestRail(testCaseId = {"5646", "5647"})
    @Description("Get IDPs for customer and get IDP by identity")
    public void getIdpCustomer() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> postResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        soft.assertThat(postResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_CREATED);
        idpIdentity = postResponse.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviders> response = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, IdentityProviders.class)
            .token(token)
            .inlineVariables(customerIdentity + "/identity-providers")).get();

        soft.assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        ResponseWrapper<IdentityProvider> responseIdentity = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CUSTOMER, IdentityProvider.class)
            .token(token)
            .inlineVariables(customerIdentity + "/identity-providers/" + idpIdentity)).get();

        soft.assertThat(responseIdentity.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(responseIdentity.getResponseEntity().getIdentity())
            .isEqualTo(idpIdentity);
        soft.assertAll();
    }
}
