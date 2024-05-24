package com.apriori.cas.api.tests;

import static com.apriori.shared.util.enums.CustomerEnum.AP_INT;
import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.cas.api.enums.CASAPIEnum;
import com.apriori.cas.api.models.response.Configurations;
import com.apriori.cas.api.utils.CasTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
@EnabledIfSystemProperty(named = "customer", matches = AP_INT)
public class CasConfigurationsTests extends TestUtil {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();
    private UserCredentials currentUser = UserUtil.getUser(APRIORI_DEVELOPER);

    @BeforeEach
    public void getToken() {
        RequestEntityUtil_Old.useTokenForRequests(currentUser.getToken());
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