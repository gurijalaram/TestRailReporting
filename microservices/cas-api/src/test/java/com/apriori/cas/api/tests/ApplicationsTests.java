package com.apriori.cas.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Applications;
import com.apriori.cas.api.models.response.Customer;
import com.apriori.cas.api.models.response.Customers;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIf(value = "com.apriori.shared.util.properties.PropertiesContext#isAPCustomer")
public class ApplicationsTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil_Old.useTokenForRequests(UserUtil.getUser(APRIORI_DEVELOPER).getToken());
    }

    @Test
    @TestRail(id = {5659})
    @Description("Returns a list of applications for the customer.")
    public void getCustomerApplications() {
        ResponseWrapper<Customers> customersResponse = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMERS, Customers.class, HttpStatus.SC_OK);

        Customer customer = customersResponse.getResponseEntity().getItems().get(0);

        ResponseWrapper<Applications> responseApplications = casTestUtil.getCommonRequest(CASAPIEnum.CUSTOMER_APPLICATIONS,
            Applications.class,
            HttpStatus.SC_OK,
            customer.getIdentity());

        soft.assertThat(responseApplications.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}