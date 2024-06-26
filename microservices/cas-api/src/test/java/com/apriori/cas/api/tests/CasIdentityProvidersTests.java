package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.IdentityProvider;
import com.apriori.cas.api.models.response.IdentityProviders;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.IdentityProviderResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class CasIdentityProvidersTests extends TestUtil {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private CustomerInfrastructure customerInfrastructure;
    private SoftAssertions soft = new SoftAssertions();
    private String customerIdentity;
    private String userIdentity;
    private String idpIdentity;
    private CdsTestUtil cdsTestUtil;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerInfrastructure = new CustomerInfrastructure(requestEntityUtil);
    }

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
    @TestRail(id = {5646, 5647})
    @Description("Get IDPs for customer and get IDP by identity")
    public void getIdpCustomer() {
        String customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        setCustomerData();

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

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        Customer newCustomer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = newCustomer.getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        ResponseWrapper<User> user = casTestUtil.createUser(newCustomer);
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
