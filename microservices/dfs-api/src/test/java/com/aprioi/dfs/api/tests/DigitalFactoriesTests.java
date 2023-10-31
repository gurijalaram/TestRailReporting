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

    private final DigitalFactoryUtil digitalFactoryUtil = new DigitalFactoryUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private static final String INVALID_SHARED_SECRET_ENDPOINT = "?key=InvalidSharedSecret";
    private static final String WITHOUT_SHARED_SECRET = "";

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

        ResponseWrapper<ErrorMessage> getComparisonsResponseWithInvalidSharedSecret = digitalFactoryUtil.findDigitalFactoriesWithInvalidSharedSecret(HttpStatusCode.UNAUTHORIZED, INVALID_SHARED_SECRET_ENDPOINT);

        softAssertions.assertThat(getComparisonsResponseWithInvalidSharedSecret.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(getComparisonsResponseWithInvalidSharedSecret.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(getComparisonsResponseWithInvalidSharedSecret.getResponseEntity().getMessage()).isEqualTo("Invalid or missing credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28964})
    @Description("Get Unauthorized Error when shared secret is not provided")
    public void findDigitalFactoriesWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> getComparisonsResponseWithoutSharedSecret = digitalFactoryUtil.findDigitalFactoriesWithInvalidSharedSecret(HttpStatusCode.UNAUTHORIZED, WITHOUT_SHARED_SECRET);

        softAssertions.assertThat(getComparisonsResponseWithoutSharedSecret.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(getComparisonsResponseWithoutSharedSecret.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(getComparisonsResponseWithoutSharedSecret.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }
}
