package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.Material;
import com.apriori.dfs.api.models.utils.MaterialUtil;
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

import java.util.HashMap;

@ExtendWith(TestRulesAPI.class)
public class MaterialsTests {

    private static final String BAD_REQUEST_ERROR = "Bad Request";
    private static final String MATERIAL_NAME = "Aluminum, ANSI 2030";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'materialIdentity' is not a valid identity.";
    private static final String PROCESS_GROUP_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'processGroupIdentity' is not a valid identity.";
    private static final String DIGITAL_FACTORY_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'digitalFactoryIdentity' is not a valid identity.";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_MATERIAL_ID = "1234567890";
    private static final String MISSING_MATERIAL_ID = "CX757P9KVW4Y";
    private static final String INVALID_OR_MISSING_CREDENTIAL_MSG = "Invalid or missing credential";
    private static final String INVALID_SHARED_SECRET = "InvalidSharedSecret";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_DIGITAL_FACTORY_ID = "ABCDEFGHIJK2";
    private static final String INVALID_DIGITAL_FACTORY_ID = "1234567890";
    private static final String MISSING_DIGITAL_FACTORY_ID = "CX757P9KVW4Y";
    private static final String VALID_PROCESS_GROUP_ID = "7EU8N44NEO5A";
    private static final String INVALID_PROCESS_GROUP_ID = "1234567890";
    private static final String MISSING_PROCESS_GROUP_ID = "CX757P9KVW4Y";
    private static final String VALID_MATERIAL_ID = "CX757P9KVW4X";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";
    private static final String NOT_FOUND = "Not Found";
    private static final String NOT_FOUND_MSG = "Resource 'Material' with identity 'CX757P9KVW4Y' was not found";
    private static final String PROCESS_GROUP_NOT_FOUND_MSG = "Resource 'ProcessGroup' with identity 'CX757P9KVW4Y' was not found";
    private static final String DIGITAL_FACTORY_NOT_FOUND_MSG = "Resource 'DigitalFactory' with identity 'CX757P9KVW4Y' was not found";
    private static final String NOT_ACCEPTABLE = "Not Acceptable";
    private static final String NOT_ACCEPTABLE_MSG = "Could not find acceptable representation";
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final MaterialUtil materialUtil = new MaterialUtil();

    @Test
    @TestRail(id = {29591})
    @Description("Gets a material by identity when shared secret/identity are valid")
    public void getMaterialByIdentityTest() {

        ResponseWrapper<Material> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.OK, Material.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getProcessGroupIdentity()).isEqualTo(VALID_PROCESS_GROUP_ID);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(MATERIAL_NAME);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29592})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getMaterialWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH_WITH_KEY_PARAM,
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29593})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getMaterialWithEmptySharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterialWithoutKeyParameter(DFSApiEnum.MATERIAL_BY_PATH_WITH_KEY_PARAM,
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29594})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getMaterialWithoutSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29597})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialWithBadIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, INVALID_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29596})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialWithMissingMaterialTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, MISSING_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29637})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialWithBadProcessGroupIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, INVALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(PROCESS_GROUP_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29636})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialWithMissingProcessGroupTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, MISSING_PROCESS_GROUP_ID, VALID_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(PROCESS_GROUP_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29639})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialWithBadDigitalFactoryIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, INVALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(DIGITAL_FACTORY_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29638})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialWithMissingDigitalFactoryTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialUtil.getMaterial(DFSApiEnum.MATERIAL_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, MISSING_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(DIGITAL_FACTORY_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29555})
    @Description("Get Not Acceptable error when incorrect Accept Header is provided")
    public void getMaterialWithIncorrectAcceptHeader() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.MATERIAL_BY_PATH, ErrorMessage.class)
            .inlineVariables(VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID)
            .headers(new HashMap<>() {
                {
                    put("Accept", "application/javascript");
                }
            });

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_ACCEPTABLE);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(NOT_ACCEPTABLE_MSG);
        softAssertions.assertAll();
    }
}
