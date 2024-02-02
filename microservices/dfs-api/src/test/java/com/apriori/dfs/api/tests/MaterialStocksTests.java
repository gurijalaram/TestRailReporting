package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.MaterialStock;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class MaterialStocksTests {

    private static final String BAD_REQUEST_ERROR = "Bad Request";
    private static final String MATERIAL_STOCK_NAME = "Square/Rectangular Bar:  1  3/4 w x 3/8 h in.  20 ft lengths";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'identity' is not a valid identity.";
    private static final String MATERIAL_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'materialIdentity' is not a valid identity.";
    private static final String PROCESS_GROUP_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'processGroupIdentity' is not a valid identity.";
    private static final String DIGITAL_FACTORY_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'digitalFactoryIdentity' is not a valid identity.";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_MATERIAL_STOCK_ID = "1234567890";
    private static final String MISSING_MATERIAL_STOCK_ID = "CX757P9KVW4Y";
    private static final String INVALID_MATERIAL_ID = "1234567890";
    private static final String MISSING_MATERIAL_ID = "CX757P9KVW4Y";
    private static final String INVALID_PROCESS_GROUP_ID = "1234567890";
    private static final String MISSING_PROCESS_GROUP_ID = "CX757P9KVW4Y";
    private static final String INVALID_DIGITAL_FACTORY_ID = "1234567890";
    private static final String MISSING_DIGITAL_FACTORY_ID = "CX757P9KVW4Y";
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
    private static final String MATERIAL_NOT_FOUND_MSG = "Resource 'Material' with identity 'CX757P9KVW4Y' was not found";
    private static final String PROCESS_GROUP_NOT_FOUND_MSG = "Resource 'ProcessGroup' with identity 'CX757P9KVW4Y' was not found";
    private static final String DIGITAL_FACTORY_NOT_FOUND_MSG = "Resource 'DigitalFactory' with identity 'CX757P9KVW4Y' was not found";
    private static final String NOT_ACCEPTABLE = "Not Acceptable";
    private static final String NOT_ACCEPTABLE_MSG = "Could not find acceptable representation";
    private static final String BOTH_PAGE_NUMBER_AND_PAGE_SIZE_MUST_BE_GREATER_THAN_0 =
        "Both pageNumber and pageSize must be greater than 0";
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final MaterialStockUtil materialStockUtil = new MaterialStockUtil();

    @Test
    @TestRail(id = {29618})
    @Description("Gets a material stock by identity when shared secret/identity are valid")
    public void getMaterialStockByIdentityTest() {

        ResponseWrapper<MaterialStock> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.OK, MaterialStock.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

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
    public void getMaterialStockWithBadMaterialStockIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, INVALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29625})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithMissingMaterialStockTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, MISSING_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29624})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithBadMaterialIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, INVALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(MATERIAL_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29623})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithMissingMaterialTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, MISSING_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(MATERIAL_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29630})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithBadProcessGroupIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, INVALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(PROCESS_GROUP_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29629})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithMissingProcessGroupTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, VALID_DIGITAL_FACTORY_ID, MISSING_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(PROCESS_GROUP_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29627})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithBadDigitalFactoryIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, INVALID_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(DIGITAL_FACTORY_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29626})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getMaterialStockWithMissingDigitalFactoryTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.getMaterialStock(DFSApiEnum.MATERIAL_STOCKS_BY_PATH,
            HttpStatusCode.NOT_FOUND, ErrorMessage.class, MISSING_DIGITAL_FACTORY_ID, VALID_PROCESS_GROUP_ID, VALID_MATERIAL_ID, VALID_MATERIAL_STOCK_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(DIGITAL_FACTORY_NOT_FOUND_MSG);
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

    @Test
    @TestRail(id = {29762})
    @Description("Get Bad Request Error when Digital factory identity is invalid")
    public void findMaterialStocksWithBadDigitalFactoryIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            INVALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(DIGITAL_FACTORY_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29761})
    @Description("Get Not Found Error when Digital factory identity is missing")
    public void findMaterialStocksWithMissingDigitalFactoryTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.NOT_FOUND,
            ErrorMessage.class,
            MISSING_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(DIGITAL_FACTORY_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29760})
    @Description("Get Bad Request Error when Process group identity is invalid")
    public void findMaterialStocksWithBadProcessGroupIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            VALID_DIGITAL_FACTORY_ID,
            INVALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(PROCESS_GROUP_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29759})
    @Description("Get Not Found Error when Process group identity is missing")
    public void findMaterialStocksWithMissingProcessGroupTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.NOT_FOUND,
            ErrorMessage.class,
            VALID_DIGITAL_FACTORY_ID,
            MISSING_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(PROCESS_GROUP_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29758})
    @Description("Get Bad Request Error when Material identity is invalid")
    public void findMaterialStocksWithBadMaterialIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            INVALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(MATERIAL_IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29757})
    @Description("Get Not Found Error when Material identity is missing")
    public void findMaterialStocksWithMissingMaterialIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.NOT_FOUND,
            ErrorMessage.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            MISSING_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(MATERIAL_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29752})
    @Description("Gets a list of material stocks by digital factory, process group and material when shared secret is valid")
    public void findMaterialStocksWithValidSharedSecretTest() {

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.OK,
            MaterialStocks.class,
            true,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29753})
    @Description("Gets no material stocks when shared secret isn't valid")
    public void findMaterialStocksWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            true,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            INVALID_SHARED_SECRET
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29755})
    @Description("Get no material stocks when shared secret is missed")
    public void findMaterialStocksWithoutSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            false,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29754})
    @Description("Gets no material stocks when shared secret is Empty")
    public void findMaterialStocksWithEmptySharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            true,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            NO_SHARED_SECRET
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29756})
    @Description("Get Not Acceptable error when incorrect Accept Header is provided")
    public void findMaterialStocksWithIncorrectAcceptHeader() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.MATERIAL_STOCKS, ErrorMessage.class)
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

    @TestRail(id = {29764})
    @Description("Find a page of Material Stocks")
    @ParameterizedTest
    @CsvSource({
        "10, 1",
        "20, 2",
        "10, 3"
    })
    public void findMaterialStocksPage(String pageSize, String pageNumber) {

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.findMaterialStocks(
            DFSApiEnum.MATERIAL_STOCKS_WITH_PAGE_SIZE_AND_PAGE_NUMBER,
            HttpStatusCode.OK,
            MaterialStocks.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            pageSize,
            pageNumber
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageNumber())
            .isEqualTo(Integer.valueOf(pageNumber));
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageSize())
            .isEqualTo(Integer.valueOf(pageSize));
        softAssertions.assertAll();
    }

    @TestRail(id = {29768})
    @Description("Find a page of Material Stocks which contains just 8 stocks" +
        " on the last page with page size is 20")
    @ParameterizedTest
    @CsvSource({
        "20, 16"
    })
    public void findMaterialStocksLastPage(String pageSize, String pageNumber) {

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.findMaterialStocks(
            DFSApiEnum.MATERIAL_STOCKS_WITH_PAGE_SIZE_AND_PAGE_NUMBER,
            HttpStatusCode.OK,
            MaterialStocks.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            pageSize,
            pageNumber
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageNumber())
            .isEqualTo(Integer.valueOf(pageNumber));
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageItemCount())
            .isLessThanOrEqualTo(Integer.valueOf(pageSize));
        softAssertions.assertThat(responseWrapper.getResponseEntity().getIsLastPage())
            .isEqualTo(true);
        softAssertions.assertAll();
    }

    @TestRail(id = {29763})
    @Description("Find invalid page number/page size of Material Stocks")
    @ParameterizedTest
    @CsvSource({
        "15, 0",
        "0, 3"
    })
    public void findMaterialStocksPageWithInvalidPageParameters(String pageSize, String pageNumber) {

        ResponseWrapper<ErrorMessage> responseWrapper = materialStockUtil.findMaterialStocks(
            DFSApiEnum.MATERIAL_STOCKS_WITH_PAGE_SIZE_AND_PAGE_NUMBER,
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            pageSize,
            pageNumber
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(BOTH_PAGE_NUMBER_AND_PAGE_SIZE_MUST_BE_GREATER_THAN_0);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29766})
    @Description("Find a page of Material Stocks matched by name")
    public void findMaterialStocksMatchedByName() {

        String searchString = "Bar";

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.findMaterialStocks(
            DFSApiEnum.MATERIAL_STOCKS_BY_NAME,
            HttpStatusCode.OK,
            MaterialStocks.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            "CN",
            searchString
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).hasSize(10);
        responseWrapper.getResponseEntity().getItems().forEach(m -> {
                softAssertions.assertThat(m.getName().toLowerCase().contains(searchString.toLowerCase())).isEqualTo(true);
            }
        );
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29767})
    @Description("Find a page of Material Stocks when name doesn't matched")
    public void findMaterialStocksWhenNameDoesNotMatched() {

        String searchString = "Test Material Stocks";

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.findMaterialStocks(
            DFSApiEnum.MATERIAL_STOCKS_BY_NAME,
            HttpStatusCode.OK,
            MaterialStocks.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            "EQ",
            searchString
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().isEmpty()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @TestRail(id = {29765})
    @Description("Find all Material Stocks sorted by name")
    @ParameterizedTest
    @ValueSource(strings = { "ASC", "DESC" })
    public void findMaterialStocksPageSortedByName(String sort) {

        int pageSize = 100;
        int pageNumber = 1;

        ResponseWrapper<MaterialStocks> responseWrapper = materialStockUtil.findMaterialStocks(
            DFSApiEnum.MATERIAL_STOCKS_SORTED_BY_NAME,
            HttpStatusCode.OK,
            MaterialStocks.class,
            VALID_DIGITAL_FACTORY_ID,
            VALID_PROCESS_GROUP_ID,
            VALID_MATERIAL_ID,
            String.valueOf(pageSize),
            String.valueOf(pageNumber),
            sort
        );

        List<MaterialStock> items = responseWrapper.getResponseEntity().getItems();

        softAssertions.assertThat(items).isNotNull();
        softAssertions.assertThat(items).hasSize(100);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageNumber()).isEqualTo(pageNumber);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageSize()).isEqualTo(pageSize);

        Comparator<String> comparator = "ASC".equals(sort) ? Comparator.naturalOrder() : Comparator.reverseOrder();
        softAssertions.assertThat(isSorted(items, comparator)).isTrue();
        softAssertions.assertAll();
    }

    private boolean isSorted(List<MaterialStock> employees, Comparator<String> materialStocksComparator) {

        if (employees.isEmpty() || employees.size() == 1) {
            return true;
        }

        Iterator<MaterialStock> iter = employees.iterator();
        MaterialStock current, previous = iter.next();
        while (iter.hasNext()) {
            current = iter.next();
            if (materialStocksComparator.compare(previous.getName(), current.getName()) > 0) {
                return false;
            }
            previous = current;
        }
        return true;
    }
}
