package com.apriori.dfs.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DESIGNER;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.DigitalFactories;
import com.apriori.dfs.api.models.response.DigitalFactory;
import com.apriori.dfs.api.models.utils.DigitalFactoryUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(TestRulesAPI.class)
public class DigitalFactoriesTests {

    private static final String BAD_REQUEST_ERROR = "Bad Request";
    private static final String NOT_FOUND_ERROR = "Not Found";
    private static final String DF_NAME = "aPriori USA";
    private static final String FORBIDDEN_ERROR = "Forbidden";
    private static final String ACCESS_DENIED_MSG =
        "Access denied: Unable to apply access control due to missing groups." +
            " Please get in touch with customer support if this is not expected.";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'identity' is not a valid identity.";
    private static final String IDENTITY_DOES_NOT_EXIST_MSG = "Resource 'DigitalFactory' with identity 'ABCDEFGHIJK5' was not found";
    private static final String INVALID_CONTENT_TYPE = "application/text";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_DIGITAL_FACTORY_ID = "1234567890";
    private static final String INVALID_OR_MISSING_CREDENTIAL_MSG = "Invalid or missing credential";
    private static final String INVALID_SHARED_SECRET = "InvalidSharedSecret";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_BASELINE_DIGITAL_FACTORY_ID = "ABCDEFGHIJK2";
    private static final String VALID_OVERLAY_DIGITAL_FACTORY_ID = "ABCDEFGHIJK4";
    private static final String NON_EXISTENT_DIGITAL_FACTORY_ID = "ABCDEFGHIJK5";
    private static final String USER_CONTEXT_NOT_FOUND_MSG = "'ap-user-context' is required";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";
    private static final String NOT_ACCEPTABLE = "Not Acceptable";
    private static final String NOT_ACCEPTABLE_MSG = "Could not find acceptable representation";
    private static final String BOTH_PAGE_NUMBER_AND_PAGE_SIZE_MUST_BE_GREATER_THAN_0 =
        "Both pageNumber and pageSize must be greater than 0";
    private static final String CONTAINS_INHERITANCE = "inheritedDigitalFactories";

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final DigitalFactoryUtil digitalFactoryUtil = new DigitalFactoryUtil();

    private final UserCredentials userCredentials = UserUtil.getUser(APRIORI_DESIGNER);

    @Test
    @TestRail(id = {28958, 31073})
    @Description("Gets a list of digital factories when shared secret is valid")
    public void findDigitalFactoriesWithValidSharedSecretTest() {

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.OK,
            DigitalFactories.class,
            userCredentials
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getBody().contains(CONTAINS_INHERITANCE)).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28961})
    @Description("Get Unauthorized Error when shared secret is not valid")
    public void findDigitalFactoriesWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            userCredentials,
            INVALID_SHARED_SECRET
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28964})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void findDigitalFactoriesWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            userCredentials,
            NO_SHARED_SECRET
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29566})
    @Description("Get Not Acceptable error when incorrect Accept Header is provided")
    public void findDigitalFactoriesWithIncorrectAcceptHeader() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.DIGITAL_FACTORIES, ErrorMessage.class)
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

    @TestRail(id = {29677})
    @Description("Find invalid page number/page size of Digital Factories")
    @ParameterizedTest
    @CsvSource({
        "15, 0",
        "0, 3"
    })
    public void findDigitalFactoriesPageWithInvalidPageNumber(String pageSize, String pageNumber) {


        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactoriesPage(
            DFSApiEnum.DIGITAL_FACTORIES_WITH_PAGE_SIZE_AND_PAGE_NUMBER,
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            userCredentials,
            pageSize,
            pageNumber
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(BOTH_PAGE_NUMBER_AND_PAGE_SIZE_MUST_BE_GREATER_THAN_0);
        softAssertions.assertAll();
    }

    @TestRail(id = {29678})
    @Description("Find a page of Digital Factories")
    @ParameterizedTest
    @CsvSource({
        "10, 1",
        "2, 2"
    })
    public void findDigitalFactoriesPage(String pageSize, String pageNumber) {

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactoriesPage(
            DFSApiEnum.DIGITAL_FACTORIES_WITH_PAGE_SIZE_AND_PAGE_NUMBER,
            HttpStatusCode.OK,
            DigitalFactories.class,
            userCredentials,
            pageSize,
            pageNumber
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageNumber()).isEqualTo(Integer.valueOf(pageNumber));
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageSize()).isEqualTo((Integer.valueOf(pageSize)));
        softAssertions.assertAll();
    }

    @TestRail(id = {29679})
    @Description("Find all Digital Factories sorted by name")
    @ParameterizedTest
    @ValueSource(strings = { "ASC", "DESC" })
    public void findDigitalFactoriesPageSortedByName(String sort) {

        int pageSize = 1000;
        int pageNumber = 1;

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactoriesPage(
            DFSApiEnum.DIGITAL_FACTORIES_SORTED_BY_NAME,
            HttpStatusCode.OK,
            DigitalFactories.class,
            userCredentials,
            sort
        );

        List<DigitalFactory> items = responseWrapper.getResponseEntity().getItems();

        softAssertions.assertThat(items).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageNumber()).isEqualTo(pageNumber);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getPageSize()).isEqualTo(pageSize);

        DigitalFactory previous = items.get(0);

        Comparator<String> comparator = "ASC".equals(sort) ? Comparator.naturalOrder() : Comparator.reverseOrder();

        for (int i = 1; i < items.size(); i++) {
            DigitalFactory current = items.get(i);
            int compareResult = comparator.compare(previous.getName(), current.getName());
            softAssertions.assertThat(compareResult).isLessThan(0);
            previous = current;
        }
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29680})
    @Description("Find a page of Digital Factories matched by name")
    public void findDigitalFactoriesMatchedByName() {

        String searchString = "apriori";

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactoriesPage(
            DFSApiEnum.DIGITAL_FACTORIES_BY_NAME,
            HttpStatusCode.OK,
            DigitalFactories.class,
            userCredentials,
            "CN",
            searchString
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        responseWrapper.getResponseEntity().getItems().forEach(pg ->
            softAssertions.assertThat(pg.getName().toLowerCase().contains(searchString)).isEqualTo(true)
        );
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29681})
    @Description("Find a page of Digital Factories not matched by name")
    public void findDigitalFactoriesNotMatchedByName() {

        String searchString = "fake";

        ResponseWrapper<DigitalFactories> responseWrapper = digitalFactoryUtil.findDigitalFactoriesPage(
            DFSApiEnum.DIGITAL_FACTORIES_BY_NAME,
            HttpStatusCode.OK,
            DigitalFactories.class,
            userCredentials,
            "EQ",
            searchString
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems().isEmpty()).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29855})
    @Description("Return Forbidden error when user not configured properly")
    public void findDigitalFactoriesByFakeUser() {

        UserCredentials fakeUser = new UserCredentials("testUser5@gadgets.aprioritest.com", "Test1");

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.FORBIDDEN,
            ErrorMessage.class,
            fakeUser
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(FORBIDDEN_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(ACCESS_DENIED_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29857})
    @Description("Find Digital Factory when user context is missed")
    public void findDigitalFactoriesWithNoUserContextTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.findDigitalFactories(
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            (UserCredentials) null
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(USER_CONTEXT_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28959, 31071})
    @Description("Gets a digital factory by identity when shared secret/identity are valid")
    public void getDigitalFactoryByIdentityTest() {

        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.OK,
            DigitalFactory.class,
            userCredentials,
            VALID_BASELINE_DIGITAL_FACTORY_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(DF_NAME);
        softAssertions.assertThat(responseWrapper.getBody().contains(CONTAINS_INHERITANCE)).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31072})
    @Description("Gets a digital factory that contains inheritance")
    public void getDigitalFactoryInheritance() {

        ResponseWrapper<DigitalFactory> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.OK,
            DigitalFactory.class,
            userCredentials,
            VALID_OVERLAY_DIGITAL_FACTORY_ID
        );

        softAssertions.assertThat(responseWrapper.getBody().contains(CONTAINS_INHERITANCE)).isEqualTo(true);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28962})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            userCredentials,
            VALID_BASELINE_DIGITAL_FACTORY_ID,
            INVALID_SHARED_SECRET
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28977})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getDigitalFactoryWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactoryWithoutKeyParameter(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            VALID_BASELINE_DIGITAL_FACTORY_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28976})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getDigitalFactoryWithEmptySharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.UNAUTHORIZED,
            ErrorMessage.class,
            userCredentials,
            VALID_BASELINE_DIGITAL_FACTORY_ID,
            NO_SHARED_SECRET
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29853})
    @Description("Get NotFound Error when requested DF is not belonged to a customer of requested user")
    public void getDigitalFactoryWithAnotherCustomerUserTest() {

        UserCredentials fakeUser = new UserCredentials("testUser5@gadgets.aprioritest.com", "Test1");

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.NOT_FOUND,
            ErrorMessage.class,
            fakeUser,
            VALID_BASELINE_DIGITAL_FACTORY_ID
        );

        String expectedMessage =
            String.format("Resource 'DigitalFactory' with identity '%s' was not found", VALID_BASELINE_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(NOT_FOUND_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(expectedMessage);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29856})
    @Description("Get Bad Request Error when user context is missed")
    public void getDigitalFactoryWithNoUserContextTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            (UserCredentials) null,
            VALID_BASELINE_DIGITAL_FACTORY_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(USER_CONTEXT_NOT_FOUND_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28978})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryWithBadIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.BAD_REQUEST,
            ErrorMessage.class,
            userCredentials,
            INVALID_DIGITAL_FACTORY_ID
        );

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {28978})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getDigitalFactoryWithNonExistentIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.getDigitalFactory(
            HttpStatusCode.NOT_FOUND,
            ErrorMessage.class,
            userCredentials,
            NON_EXISTENT_DIGITAL_FACTORY_ID
        );

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
            HttpStatusCode.CREATED,
            DigitalFactory.class,
            createRequestBody()
        );

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
            HttpStatusCode.NO_CONTENT, null, VALID_BASELINE_DIGITAL_FACTORY_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity()).isNull();
        softAssertions.assertThat(responseWrapper.getBody()).isEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29151})
    @Description("Delete Unauthorized Error when shared secret value is invalid")
    public void deleteDigitalFactoryWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.deleteDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_BASELINE_DIGITAL_FACTORY_ID, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29152})
    @Description("Delete Unauthorized Error when shared secret value is not provided")
    public void deleteDigitalFactoryWithEmptySharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = digitalFactoryUtil.deleteDigitalFactory(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_BASELINE_DIGITAL_FACTORY_ID, NO_SHARED_SECRET);

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
