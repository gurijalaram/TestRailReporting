package com.apriori.ats.api.tests;

import com.apriori.ats.api.utils.AtsTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.IdentityProviderResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AtsAuthenticationTests extends TestUtil {
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<User> user;
    private ResponseWrapper<IdentityProviderResponse> identityProvider;
    private String customerIdentity;
    private String customerName;
    private String userIdentity;
    private String idpIdentity;

    @AfterEach
    public void cleanUp() {
        if (idpIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.SAML_BY_CUSTOMER_PROVIDER_IDS, customerIdentity, idpIdentity);
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {22084})
    @Description("Authenticate with email and password.")
    public void authenticateUserTest() {
        UserCredentials userCredentials = UserUtil.getUser();
        String userEmail = userCredentials.getEmail();
        String userPassword = userCredentials.getPassword();
        ResponseWrapper<User> authenticate = atsTestUtil.authenticateUser(userEmail, userPassword);

        soft.assertThat(authenticate.getResponseEntity().getEmail()).isEqualTo(userEmail);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {22083})
    @Description("Creates a user for SAML federated providers")
    public void createUserForSaml() {
        setCustomerData();
        identityProvider = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        idpIdentity = identityProvider.getResponseEntity().getIdentity();

        ResponseWrapper<User> createSamlUser = atsTestUtil.putSAMLProviders(customerName);

        soft.assertThat(createSamlUser.getResponseEntity().getIdentity()).isNotEmpty();
        soft.assertThat(createSamlUser.getResponseEntity().getUserType()).isEqualTo("AP_SAML_USER");
        soft.assertAll();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerName = customer.getResponseEntity().getName();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        String userName = generateStringUtil.generateUserName();
        user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}