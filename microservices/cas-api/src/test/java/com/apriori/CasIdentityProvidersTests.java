package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.IdentityProvider;
import com.apriori.cas.models.response.IdentityProviders;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.IdentityProviderResponse;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.User;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CasIdentityProvidersTests extends TestUtil {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
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
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        description = customerName + " Description";
        email = customerName.toLowerCase();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<User> user = CasTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @AfterEach
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
    @TestRail(id = {5646, 5647})
    @Description("Get IDPs for customer and get IDP by identity")
    public void getIdpCustomer() {
        String customerName = generateStringUtil.generateCustomerName();

        ResponseWrapper<IdentityProviderResponse> postResponse = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        idpIdentity = postResponse.getResponseEntity().getIdentity();

        ResponseWrapper<IdentityProviders> response = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, IdentityProviders.class, HttpStatus.SC_OK, customerIdentity + "/identity-providers");

        soft.assertThat(response.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);

        ResponseWrapper<IdentityProvider> responseIdentity = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER, IdentityProvider.class, HttpStatus.SC_OK, customerIdentity + "/identity-providers/" + idpIdentity);

        soft.assertThat(responseIdentity.getResponseEntity().getIdentity())
            .isEqualTo(idpIdentity);
        soft.assertAll();
    }
}
