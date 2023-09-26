package com.apriori;

import com.apriori.ats.models.response.UserByEmail;
import com.apriori.ats.utils.AtsTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.IdentityProviderResponse;
import com.apriori.cds.models.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.Customer;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AtsAuthenticationTests {
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private ResponseWrapper<IdentityProviderResponse> identityProvider;
    private String customerIdentity;
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
        ResponseWrapper<UserByEmail> authenticate = atsTestUtil.authenticateUser(userEmail, userPassword);

        soft.assertThat(authenticate.getResponseEntity().getEmail()).isEqualTo(userEmail);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {22083})
    @Description("Creates a user for SAML federated providers")
    public void createUserForSaml() {
        String customerName = generateStringUtil.generateCustomerName();
        String userName = generateStringUtil.generateUserName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
        identityProvider = cdsTestUtil.addSaml(customerIdentity, userIdentity, customerName);
        idpIdentity = identityProvider.getResponseEntity().getIdentity();

        ResponseWrapper<UserByEmail> createSamlUser = atsTestUtil.putSAMLProviders(emailPattern);

        soft.assertThat(createSamlUser.getResponseEntity().getIdentity()).isNotEmpty();
        soft.assertThat(createSamlUser.getResponseEntity().getUserType()).isEqualTo("AP_SAML_USER");
        soft.assertAll();
    }
}