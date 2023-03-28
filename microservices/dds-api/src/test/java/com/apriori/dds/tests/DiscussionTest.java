package com.apriori.dds.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.request.SearchDiscussionsRequest;
import entity.response.DiscussionResponse;
import entity.response.DiscussionsResponse;
import entity.response.ErrorMessage;
import enums.DDSApiEnum;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import utils.DdsApiTestUtils;

import java.util.Collections;

public class DiscussionTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DiscussionResponse> discussionResponse;
    private static String discussionDescription = StringUtils.EMPTY;


    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(UserUtil.getUser().getEmail());
        discussionResponse = DdsApiTestUtils.createDiscussion(discussionDescription, userContext);
    }

    @Test
    @TestRail(testCaseId = {"12404"})
    @Description("create a valid discussion")
    public void createDiscussions() {
        softAssertions.assertThat(discussionResponse.getResponseEntity().getDescription()).isEqualTo(discussionDescription);
    }


    @Test
    @TestRail(testCaseId = {"12405"})
    @Description("create a discussion with invalid status")
    public void createDiscussionWithInvalidStatus() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ARCHIVED")
                .description(RandomStringUtils.randomAlphabetic(12))
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSIONS, ErrorMessage.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .headers(DdsApiTestUtils.setUpHeader())
            .body(discussionsRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CONFLICT);

        ResponseWrapper<ErrorMessage> errorMessageResponseWrapper = HTTPRequest.build(requestEntity).post();
        softAssertions.assertThat(errorMessageResponseWrapper.getResponseEntity().getMessage()).contains("Status should be ACTIVE for all new discussions");
    }

    @Test
    @TestRail(testCaseId = {"12406"})
    @Description("Get all discussions")
    public void getDiscussions() {
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSIONS, DiscussionsResponse.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .headers(DdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionsResponse> discussionsResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(discussionsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"12407"})
    @Description("update a valid discussion")
    public void UpdateValidDiscussion() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("RESOLVED")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_DISCUSSION, DiscussionResponse.class)
            .inlineVariables(PropertiesContext.get("customer_identity"), discussionResponse.getResponseEntity().getIdentity())
            .headers(DdsApiTestUtils.setUpHeader())
            .body(discussionsRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionResponse> discussionResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(discussionResponse.getResponseEntity().getStatus()).isEqualTo("RESOLVED");
    }


    @Test
    @TestRail(testCaseId = {"12408"})
    @Description("update a invalid discussion")
    public void UpdateInValidDiscussion() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("RESOLVED")
                .build())
            .build();

        ResponseWrapper<ErrorMessage> discussionResponse = HTTPRequest.build(RequestEntityUtil
                .init(DDSApiEnum.CUSTOMER_DISCUSSION, ErrorMessage.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), "FDAEINVALID")
                .headers(DdsApiTestUtils.setUpHeader())
                .body(discussionsRequest)
                .apUserContext(userContext)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST))
            .patch();

        softAssertions.assertThat(discussionResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"12409", "12412"})
    @Description("Create, delete and update a deleted discussion")
    public void UpdateDeletedDiscussion() {
        ResponseWrapper<DiscussionResponse> discussionCreatedResponse = DdsApiTestUtils.createDiscussion(discussionDescription, userContext);
        softAssertions.assertThat(discussionResponse.getResponseEntity().getDescription()).isEqualTo(discussionDescription);

        ResponseWrapper<String> discussionDeletedResponse = DdsApiTestUtils.deleteDiscussion(discussionCreatedResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertThat(discussionDeletedResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);

        ResponseWrapper<ErrorMessage> discussionResponse = HTTPRequest.build(RequestEntityUtil
                .init(DDSApiEnum.CUSTOMER_DISCUSSION, ErrorMessage.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), discussionCreatedResponse.getResponseEntity().getIdentity())
                .headers(DdsApiTestUtils.setUpHeader())
                .body(DiscussionsRequest.builder()
                    .discussion(DiscussionsRequestParameters.builder()
                        .status("RESOLVED")
                        .build())
                    .build())
                .apUserContext(userContext)
                .expectedResponseCode(HttpStatus.SC_NOT_FOUND))
            .patch();

        softAssertions.assertThat(discussionResponse.getResponseEntity().getMessage()).contains("Resource 'Discussion' with identity '" + discussionCreatedResponse.getResponseEntity().getIdentity() + "' was not found");
    }

    @Test
    @TestRail(testCaseId = {"12413"})
    @Description("delete a invalid discussion")
    public void deleteInvalidDiscussion() {
        ResponseWrapper<ErrorMessage> discussionDeletedResponse = HTTPRequest.build(RequestEntityUtil
                .init(DDSApiEnum.CUSTOMER_DISCUSSION, ErrorMessage.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), "FDWXINVALID")
                .headers(DdsApiTestUtils.setUpHeader())
                .apUserContext(userContext)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST))
            .delete();

        softAssertions.assertThat(discussionDeletedResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"14327"})
    @Description("Create Discussion with invalid Customer Identity")
    public void createDiscussionWithInvalidCustomer() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(RandomStringUtils.randomAlphabetic(12))
                .build())
            .build();

        ResponseWrapper<ErrorMessage> errorMessageResponseWrapper = HTTPRequest.build(RequestEntityUtil
                .init(DDSApiEnum.CUSTOMER_DISCUSSIONS, ErrorMessage.class)
                .inlineVariables("INVALIDCUSTOMER")
                .headers(DdsApiTestUtils.setUpHeader())
                .body(discussionsRequest)
                .apUserContext(userContext)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST))
            .post();

        softAssertions.assertThat(errorMessageResponseWrapper.getResponseEntity().getMessage()).contains("'customerIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"14328"})
    @Description("Get Discussion with invalid Discussion Identity")
    public void getDiscussionWithInvalidIdentity() {
        ResponseWrapper<ErrorMessage> discussionErrorResponse = HTTPRequest.build(RequestEntityUtil
                .init(DDSApiEnum.CUSTOMER_DISCUSSION, ErrorMessage.class)
                .inlineVariables(PropertiesContext.get("customer_identity"), "FDAEINVALID")
                .headers(DdsApiTestUtils.setUpHeader())
                .apUserContext(userContext)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST))
            .get();

        softAssertions.assertThat(discussionErrorResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @Ignore
    @TestRail(testCaseId = {"12410"})
    @Description("Search discussions")
    public void searchDiscussions() {
        SearchDiscussionsRequest searchDiscussionsRequest = SearchDiscussionsRequest.builder()
            .discussionIds(Collections.singletonList(discussionResponse.getResponseEntity().getIdentity())).build();
        RequestEntity requestEntity = RequestEntityUtil.init(DDSApiEnum.CUSTOMER_SEARCH_DISCUSSIONS, DiscussionsResponse.class)
            .inlineVariables(PropertiesContext.get("customer_identity"))
            .headers(DdsApiTestUtils.setUpHeader())
            .body(searchDiscussionsRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DiscussionsResponse> discussionsResponse = HTTPRequest.build(requestEntity).post();
        softAssertions.assertThat(discussionsResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        DdsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertAll();
    }
}
