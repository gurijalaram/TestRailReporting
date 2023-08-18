package com.apriori;

import com.apriori.dds.enums.DDSApiEnum;
import com.apriori.dds.models.request.DiscussionsRequest;
import com.apriori.dds.models.request.DiscussionsRequestParameters;
import com.apriori.dds.models.request.SearchDiscussionsRequest;
import com.apriori.dds.models.response.DiscussionResponse;
import com.apriori.dds.models.response.DiscussionsResponse;
import com.apriori.dds.utils.DdsApiTestUtils;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ErrorMessage;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class DiscussionTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DiscussionResponse> discussionResponse;
    private static String discussionDescription = StringUtils.EMPTY;


    @BeforeEach
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(UserUtil.getUser().getEmail());
        discussionResponse = DdsApiTestUtils.createDiscussion(discussionDescription, userContext);
    }

    @Test
    @TestRail(id = {12404})
    @Description("create a valid discussion")
    public void createDiscussions() {
        softAssertions.assertThat(discussionResponse.getResponseEntity().getDescription()).isEqualTo(discussionDescription);
    }

    @Test
    @TestRail(id = {12405})
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
    @TestRail(id = {12406})
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
    @TestRail(id = {12407})
    @Description("update a valid discussion")
    public void updateValidDiscussion() {
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
    @TestRail(id = {12408})
    @Description("update a invalid discussion")
    public void updateInValidDiscussion() {
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
    @TestRail(id = {12409, 12412})
    @Description("Create, delete and update a deleted discussion")
    public void updateDeletedDiscussion() {
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
    @TestRail(id = {12413})
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
    @TestRail(id = {14327})
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
    @TestRail(id = {14328})
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
    @Disabled
    @TestRail(id = {12410})
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

    @AfterEach
    public void testCleanup() {
        DdsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertAll();
    }
}