package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.ApplicationMetadata;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class AchApplicationDataTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
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