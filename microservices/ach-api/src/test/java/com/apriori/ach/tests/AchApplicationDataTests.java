package com.apriori.ach.tests;

import com.apriori.ach.entity.response.ApplicationMetadata;
import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class AchApplicationDataTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = {"21957"})
    @Description("Returns the current application's metadata.")
    public void getAppMetadata() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<ApplicationMetadata> appMetadata = achTestUtil.getCommonRequest(ACHAPIEnum.APP_METADATA, ApplicationMetadata.class, HttpStatus.SC_OK);

        soft.assertThat(appMetadata.getResponseEntity().getApplicationName()).isEqualTo("aPriori Cloud Home");
        soft.assertAll();
    }
}