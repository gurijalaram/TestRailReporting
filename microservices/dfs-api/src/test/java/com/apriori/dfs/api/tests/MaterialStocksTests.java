package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.MaterialStocks;
import com.apriori.dfs.api.models.utils.MaterialStockUtil;
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
public class MaterialStocksTests {

    private static final String BAD_REQUEST_ERROR = "Bad Request";
//    private static final String MATERIAL_NAME = "Aluminum, ANSI 2030";
    private static final String MATERIAL_STOCK_NAME = "Square/Rectangular Bar:  1  3/4 w x 3/8 h in.  20 ft lengths";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'identity' is not a valid identity.";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_MATERIAL_STOCK_ID = "1234567890";
    private static final String MISSING_MATERIAL_STOCK_ID = "CX757P9KVW4Y";
    private static final String INVALID_OR_MISSING_CREDENTIAL_MSG = "Invalid or missing credential";
    private static final String INVALID_SHARED_SECRET = "InvalidSharedSecret";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_DIGITAL_FACTORY_ID = "ABCDEFGHIJK2";
    private static final String VALID_PROCESS_GROUP_ID = "7EU8N44NFQ5K";
    private static final String VALID_MATERIAL_ID = "CX754MUJNPHC";
    private static final String VALID_MATERIAL_STOCK_ID = "CXTAPM75DJTI";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";
    private static final String NOT_FOUND = "Not Found";
    private static final String NOT_FOUND_MSG = "Resource 'MaterialStock' with identity 'CX757P9KVW4Y' was not found";
    private static final String NOT_ACCEPTABLE = "Not Acceptable";
    private static final String NOT_ACCEPTABLE_MSG = "Could not find acceptable representation";
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final MaterialStockUtil materialStockUtil = new MaterialStockUtil();

    @Test
    @TestRail(id = {29618})
    @Description("Gets a material stock by identity when shared secret/identity are valid")
    public void getMaterialStockByIdentityTest() {

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.OK, MaterialStocks.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMaterialIdentity()).isEqualTo(VALID_MATERIAL_ID);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(MATERIAL_STOCK_NAME);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29619})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getMaterialStockWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH_WITH_KEY_PARAM,
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29620})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getMaterialStockWithEmptySharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStockWithoutKeyParameter(DFSApiEnum.MATERIAL_STOCKS_BY_PATH_WITH_KEY_PARAM,
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29621})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getMaterialStockWithoutSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29628})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithBadIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, INVALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29625})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithMissingMaterialTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, MISSING_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29622})
    @Description("Get Not Acceptable error when incorrect Accept Header is provided")
    public void getMaterialStockWithIncorrectAcceptHeader() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.MATERIAL_STOCKS_BY_PATH, ErrorMessage.class)
            .inlineVariables(VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID)
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
