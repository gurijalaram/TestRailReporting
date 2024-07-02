package com.apriori.cas.api.tests;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Configurations;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
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
public class CasConfigurationsTests extends TestUtil {
    private CasTestUtil casTestUtil;
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser()
            .useTokenInRequests();
        casTestUtil = new CasTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {5660})
    @Description("Returns a list of all aP Versions.")
    public void getConfigurationsTest() {
        ResponseWrapper<Configurations> configurationsResponse = casTestUtil.getCommonRequest(CASAPIEnum.CONFIGURATIONS, Configurations.class, HttpStatus.SC_OK);

        soft.assertThat(configurationsResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}