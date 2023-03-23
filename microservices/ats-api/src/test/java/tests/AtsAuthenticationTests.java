package tests;

import com.apriori.ats.entity.response.UserByEmail;
import com.apriori.ats.utils.AtsTestUtil;
import com.apriori.cds.entity.response.IdentityProviderResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

public class AtsAuthenticationTests {
    private AtsTestUtil atsTestUtil = new AtsTestUtil();
    SoftAssertions soft = new SoftAssertions();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    ResponseWrapper<IdentityProviderResponse> identityProvider;
    private String customerIdentity;
    private String userIdentity;
    private String idpIdentity;

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
    @TestRail(testCaseId = {"22084"})
    @Description("Authenticate with email and password.")
    public void authenticateUserTest() {
        UserCredentials userCreds = UserUtil.getUser();
        String userEmail = userCreds.getEmail();
        String userPassword = userCreds.getPassword();
        ResponseWrapper<UserByEmail> authenticate = atsTestUtil.authenticateUser(userEmail, userPassword);

        soft.assertThat(authenticate.getResponseEntity().getEmail()).isEqualTo(userEmail);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22083"})
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