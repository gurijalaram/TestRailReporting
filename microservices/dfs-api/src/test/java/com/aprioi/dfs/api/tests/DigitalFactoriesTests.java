package com.aprioi.dfs.api.tests;

import com.apriori.dfs.api.models.utils.DigitalFactoryUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.response.ErrorMessage;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.http.HttpStatusCode;

@ExtendWith(TestRulesAPI.class)
public class DigitalFactoriesTests {

    private DigitalFactoryUtil digitalFactoryUtil = new DigitalFactoryUtil();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {})
    @Description("Gets a list of digital factories for a specific customer")
    public void getDigitalFactoryTest() {

        ResponseWrapper<ErrorMessage> getComparisonsResponse = digitalFactoryUtil.getDigitalFactories(HttpStatusCode.UNAUTHORIZED);

        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getError()).isEqualTo("Unauthorized");

        softAssertions.assertAll();
    }
}
