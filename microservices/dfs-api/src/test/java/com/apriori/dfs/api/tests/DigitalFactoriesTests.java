package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.DigitalFactories;
import com.apriori.dfs.api.models.response.DigitalFactory;
import com.apriori.dfs.api.models.utils.DigitalFactoryUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
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

    private static final String BAD_REQUEST_ERROR = "Bad Request";
    private static final String NOT_FOUND_ERROR = "Not Found";
    private static final String DF_NAME = "aPriori USA";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'identity' is not a valid identity.";
    private static final String IDENTITY_DOES_NOT_EXIST_MSG = "Resource 'DigitalFactory' with identity 'ABCDEFGHIJK5' was not found";
    private static final String INVALID_CONTENT_TYPE = "application/text";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_DIGITAL_FACTORY_ID = "1234567890";
    private static final String INVALID_OR_MISSING_CREDENTIAL_MSG = "Invalid or missing credential";
    private static final String INVALID_SHARED_SECRET = "InvalidSharedSecret";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_DIGITAL_FACTORY_ID = "ABCDEFGHIJK2";
    private static final String NON_EXISTENT_DIGITAL_FACTORY_ID = "ABCDEFGHIJK5";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";
    private static final String NOT_ACCEPTABLE = "Not Acceptable";
    private static final String NOT_ACCEPTABLE_MSG = "Could not find acceptable representation";

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final DigitalFactoryUtil digitalFactoryUtil = new DigitalFactoryUtil();

    @Test
    @TestRail(id = {28958})
    @Description("Gets a list of digital factories when shared secret is valid")
    public void findDigitalFactoriesWithValidSharedSecretTest() {

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.OK, DigitalFactories.class);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28961})
    @Description("Get Unauthorized Error when shared secret is not valid")
    public void findDigitalFactoriesWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28964})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void findDigitalFactoriesWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28959})
    @Description("Gets a digital factory by identity when shared secret/identity are valid")
    public void getDigitalFactoryByIdentityTest() {

        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.OK, DigitalFactory.class, VALID_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(DF_NAME);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28962})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28977})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getDigitalFactoryWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactoryWithoutKeyParameter(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28976})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getDigitalFactoryWithEmptySharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28978})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryWithBadIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, INVALID_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28978})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryWithNonExistentIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, NON_EXISTENT_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_DOES_NOT_EXIST_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28978})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryIncorrectAcceptHeader() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.NOT_ACCEPTABLE, ErrorMessage.class, NON_EXISTENT_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_DOES_NOT_EXIST_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29566})
    @Description("Get Not Acceptable error when incorrect Accept Header is provided")
    public void getDigitalFactoryWithIncorrectAcceptHeader() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, ErrorMessage.class)
            .headers(new HashMap<>() {
                {
                    put("Accept", "application/javascript");
                }
            });

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getStatus()).isEqualTo(406);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_ACCEPTABLE);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(NOT_ACCEPTABLE_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28960})
    @Description("Upsert DigitalFactory when shared secret/identity is valid")
    public void upsertDigitalFactoryWithValidSharedSecretTest() {

        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.upsertDigitalFactory(
            HttpStatusCode.CREATED, DigitalFactory.class, createRequestBody());

        softAssertions.assertThat(responseWrapper.getResponseEntity().getIdentity()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(DF_NAME);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29139})
    @Description("Upsert DigitalFactory with BadRequest Error when requestBody is missing")
    public void upsertDigitalFactoryWithMissingRequestBodyTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactory(
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, Collections.emptyMap());

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo("'requestBody' does not contain a root node named 'vpe'.");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29140})
    @Description("Upsert DigitalFactory with UnSupported Media Type Error when Content-Type not supported")
    public void upsertDigitalFactoryWithUnSupportedMediaTypeTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactory(
            415, ErrorMessage.class, null, INVALID_CONTENT_TYPE);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo("Unsupported Media Type");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29141})
    @Description("Upsert DigitalFactory Unauthorized Error when shared secret is Invalid")
    public void upsertDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, createRequestBody(), null, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29142})
    @Description("Upsert DigitalFactory Unauthorized Error when shared secret parameter is not provided")
    public void upsertDigitalFactoryWithoutSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.upsertDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, createRequestBody(), null, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29150})
    @Description("Delete a digital factory by identity when shared secret/identity are valid")
    public void deleteDigitalFactoryByIdentityTest() {

        ResponseWrapper<Void> responseWrapper = digitalFactoryUtil.deleteDigitalFactory(
            HttpStatusCode.NO_CONTENT, null, VALID_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity()).isNull();
        softAssertions.assertThat(responseWrapper.getBody()).isEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29151})
    @Description("Delete Unauthorized Error when shared secret value is invalid")
    public void deleteDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.deleteDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29152})
    @Description("Delete Unauthorized Error when shared secret value is not provided")
    public void deleteDigitalFactoryWithEmptySharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.deleteDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29153})
    @Description("Delete Bad Request Error when identity is invalid")
    public void deleteDigitalFactoryWithBadIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.deleteDigitalFactory(
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, INVALID_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
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
