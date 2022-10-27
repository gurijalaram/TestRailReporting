package com.apriori.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.entity.response.Configurations;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class CasConfigurationsTests extends TestUtil {
    private SoftAssertions soft = new SoftAssertions();
    private String token;

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @Test
    @TestRail(testCaseId = {"5660"})
    @Description("Returns a list of all aP Versions.")
    public void getConfigurationsTest() {
        ResponseWrapper<Configurations> configurationsResponse = HTTPRequest.build(RequestEntityUtil.init(CASAPIEnum.CONFIGURATIONS, Configurations.class)
            .token(token)).get();

        soft.assertThat(configurationsResponse.getStatusCode())
            .isEqualTo(HttpStatus.SC_OK);
        soft.assertThat(configurationsResponse.getResponseEntity().getTotalItemCount())
            .isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}