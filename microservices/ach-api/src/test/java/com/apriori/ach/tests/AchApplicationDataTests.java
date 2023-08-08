package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.ApplicationMetadata;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AchApplicationDataTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = {21957})
    @Description("Returns the current application's metadata.")
    public void getAppMetadata() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<ApplicationMetadata> appMetadata = achTestUtil.getCommonRequest(ACHAPIEnum.APP_METADATA, ApplicationMetadata.class, HttpStatus.SC_OK);

        soft.assertThat(appMetadata.getResponseEntity().getApplicationName()).isEqualTo("aPriori Cloud Home");
        soft.assertAll();
    }
}