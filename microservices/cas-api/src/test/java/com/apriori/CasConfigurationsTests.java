package com.apriori;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.entity.response.Configurations;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasConfigurationsTests extends TestUtil {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
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