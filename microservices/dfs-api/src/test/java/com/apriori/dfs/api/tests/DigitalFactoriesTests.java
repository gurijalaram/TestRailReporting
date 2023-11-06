package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.models.response.DigitalFactories;
import com.apriori.dfs.api.models.response.DigitalFactory;
import com.apriori.dfs.api.models.utils.DigitalFactoryUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(TestRulesAPI.class)
public class DigitalFactoriesTests {

    private final DigitalFactoryUtil digitalFactoryUtil = new DigitalFactoryUtil();
    private final SoftAssertions softAssertions = new SoftAssertions();
    private static final String INVALID_SHARED_SECRET = "?key=InvalidSharedSecret";
    private static final String EMPTY_SHARED_SECRET = "?key=";
    private static final String VALID_SHARED_SECRET = "?key=DogCatMonkey";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_CONTENT_TYPE = "application/json";
    private static final String INVALID_CONTENT_TYPE = "application/text";

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
    @TestRail(id = {28959})
    @Description("Gets a digital factory by identity when shared secret/identity are valid")
    public void getDigitalFactoryByIdentityTest() {

        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.getDigitalFactory("123456789012");

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo("aPriori USA");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28962})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.UNAUTHORIZED, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid or missing credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28977})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getDigitalFactoryWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.UNAUTHORIZED, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28976})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getDigitalFactoryWithEmptySharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.UNAUTHORIZED, EMPTY_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28978})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryWithBadIdentityTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(HttpStatusCode.BAD_REQUEST,"1234567890");

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(400);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("'identity' is not a valid identity.");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28960})
    @Description("Upsert DigitalFactory when shared secret/identity is valid")
    public void upsertDigitalFactoryWithValidSharedSecretTest() {
        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.upsertDigitalFactory(createRequestBody());

        softAssertions.assertThat(responseWrapper.getResponseEntity().getIdentity()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo("aPriori USA");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29139})
    @Description("Upsert DigitalFactory with BadRequest Error when requestBody is missing")
    public void upsertDigitalFactoryWithMissingRequestBodyTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactoryWithInvalidRequest(Collections.emptyMap(), HttpStatusCode.BAD_REQUEST, VALID_SHARED_SECRET, VALID_CONTENT_TYPE);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(400);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Bad Request");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("'requestBody' does not contain a root node named 'vpe'.");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29140})
    @Description("Upsert DigitalFactory with UnSupported Media Type Error when Content-Type not supported")
    public void upsertDigitalFactoryWithUnSupportedMediaTypeTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactoryWithInvalidRequest(null, 415, VALID_SHARED_SECRET, INVALID_CONTENT_TYPE);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(415);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unsupported Media Type");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29141})
    @Description("Upsert DigitalFactory Unauthorized Error when shared secret is Invalid")
    public void upsertDigitalFactoryWithInvalidSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactoryWithInvalidRequest(createRequestBody(), HttpStatusCode.UNAUTHORIZED, INVALID_SHARED_SECRET, VALID_CONTENT_TYPE);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid or missing credential");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29142})
    @Description("Upsert DigitalFactory Unauthorized Error when shared secret parameter is not provided")
    public void upsertDigitalFactoryWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactoryWithInvalidRequest(createRequestBody(), HttpStatusCode.UNAUTHORIZED, NO_SHARED_SECRET, VALID_CONTENT_TYPE);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(401);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unauthorized");
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("Invalid credential");
        softAssertions.assertAll();
    }

    private Map<String, Object> createRequestBody() {
        Map<String, Object> digitalFactory = new HashMap<>();
        digitalFactory.put("name", "Test DF");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vpe", digitalFactory);
        return requestBody;
    }
}
