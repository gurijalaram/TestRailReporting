package com.apriori.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.entity.response.Configurations;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class CasConfigurationsTests extends TestUtil {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = {"5660"})
    @Description("Returns a list of all aP Versions.")
    public void getConfigurationsTest() {
        ResponseWrapper<Configurations> configurationsResponse = casTestUtil.getCommonRequest(CASAPIEnum.CONFIGURATIONS, Configurations.class);

        soft.assertThat(configurationsResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(configurationsResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}