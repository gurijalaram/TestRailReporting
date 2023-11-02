package com.aprioi.dfs.api.tests;

import com.apriori.dfs.api.models.response.DigitalFactories;
import com.apriori.dfs.api.models.response.DigitalFactory;
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
    private static final String INVALID_SHARED_SECRET = "?key=InvalidSharedSecret";
    private static final String EMPTY_SHARED_SECRET = "?key=";

    @Test
    @TestRail(id = {28962})
    @Description("Gets a specific digital factory for a specific customer")
    public void getDigitalFactoryTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactories(HttpStatusCode.UNAUTHORIZED);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28958})
    @Description("Gets a list of digital factories when shared secret is valid")
    public void findDigitalFactoriesWithValidSharedSecretTest() {

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactories();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28961})
    @Description("Get Unauthorized Error when shared secret is not valid")
    public void findDigitalFactoriesWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactoriesWithInvalidSharedSecret(HttpStatusCode.UNAUTHORIZED, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid or missing credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28964})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void findDigitalFactoriesWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactoriesWithInvalidSharedSecret(HttpStatusCode.UNAUTHORIZED, "");

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {1})
    @Description("Gets a digital factory by identity when shared secret/identity are valid")
    public void getDigitalFactoryByIdentityTest() {

        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.getDigitalFactory("123456789012");

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo("aPriori USA");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {2})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.UNAUTHORIZED, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid or missing credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {3})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getDigitalFactoryWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.UNAUTHORIZED, "");

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {4})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getDigitalFactoryWithEmptySharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.UNAUTHORIZED, EMPTY_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {5})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryWithBadIdentityTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.BAD_REQUEST,"1234567890");

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(400);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("'identity' is not a valid identity.");
        softAssertions.assertAll();
    }
}
