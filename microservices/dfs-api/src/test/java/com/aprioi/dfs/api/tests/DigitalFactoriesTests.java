package com.aprioi.dfs.api.tests;

import com.apriori.dfs.api.models.response.DigitalFactories;
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
    @TestRail(id = {28962})
    @Description("Gets a specific digital factory for a specific customer")
    public void getDigitalFactoryTest() {

        ResponseWrapper<ErrorMessage> getComparisonsResponse = digitalFactoryUtil.getDigitalFactories(HttpStatusCode.UNAUTHORIZED);

        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getError()).isEqualTo("Unauthorized");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28958})
    @Description("Gets a list of digital factories when shared secret is valid")
    public void findDigitalFactoriesWithValidSharedSecretTest() {

        ResponseWrapper<DigitalFactories> getComparisonsResponse = digitalFactoryUtil.findDigitalFactories();

        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28961})
    @Description("Get Unauthorized Error when shared secret is not valid")
    public void findDigitalFactoriesWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> getComparisonsResponse = digitalFactoryUtil.findDigitalFactoriesWithInvalidSharedSecret(HttpStatusCode.UNAUTHORIZED);

        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getMessage()).isEqualTo("Invalid or missing credential");
        softAssertions.assertAll();

        getComparisonsResponse = digitalFactoryUtil.findDigitalFactoriesWithoutSharedSecret(HttpStatusCode.UNAUTHORIZED);

        softAssertions.assertThat(getComparisonsResponse.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }
}
