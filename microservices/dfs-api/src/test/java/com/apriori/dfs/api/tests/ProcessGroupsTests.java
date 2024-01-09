package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.enums.DFSApiEnum;
import com.apriori.dfs.api.models.response.ProcessGroup;
import com.apriori.dfs.api.models.response.ProcessGroups;
import com.apriori.dfs.api.models.utils.ProcessGroupsUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
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
public class ProcessGroupsTests {

    private static final String BAD_REQUEST_ERROR = "Bad Request";
    private static final String PG_NAME = "Stock Machining";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'identity' is not a valid identity.";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_PROCESS_GROUP_ID = "1234567890";
    private static final String INVALID_OR_MISSING_CREDENTIAL_MSG = "Invalid or missing credential";
    private static final String INVALID_SHARED_SECRET = "InvalidSharedSecret";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_PROCESS_GROUP_ID = "7EU8N44NCK0F";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";
    private static final String NOT_ACCEPTABLE = "Not Acceptable";
    private static final String NOT_ACCEPTABLE_MSG = "Could not find acceptable representation";
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final ProcessGroupsUtil processGroupsUtil = new ProcessGroupsUtil();
    protected static RequestEntityUtil requestEntityUtil;

    @Test
    @TestRail(id = {29414})
    @Description("Gets a process group by identity when shared secret/identity are valid")
    public void getProcessGroupByIdentityTest() {

        ResponseWrapper<ProcessGroup> responseWrapper = processGroupsUtil.getProcessGroup(
            HttpStatusCode.OK, ProcessGroup.class, VALID_PROCESS_GROUP_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(PG_NAME);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29415})
    @Description("Get Unauthorized Error when shared secret value is invalid")
    public void getProcessGroupWithInvalidSharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = processGroupsUtil.getProcessGroup(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_PROCESS_GROUP_ID, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29417})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void getProcessGroupWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = processGroupsUtil.getProcessGroupWithoutKeyParameter(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_PROCESS_GROUP_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29416})
    @Description("Get Unauthorized Error when shared secret value is not provided")
    public void getProcessGroupWithEmptySharedSecretTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = processGroupsUtil.getProcessGroup(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, VALID_PROCESS_GROUP_ID, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29418})
    @Description("Get Unauthorized Error when identity is invalid")
    public void getProcessGroupWithBadIdentityTest() {

        ResponseWrapper<ErrorMessage> responseWrapper = processGroupsUtil.getProcessGroup(
            HttpStatusCode.BAD_REQUEST, ErrorMessage.class, INVALID_PROCESS_GROUP_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(BAD_REQUEST_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29552})
    @Description("Find a list of Process Groups")
    public void findProcessGroupswithValidSharedSecret() {

        ResponseWrapper<ProcessGroups> responseWrapper = processGroupsUtil.findProcessGroups(
            HttpStatusCode.OK, ProcessGroups.class);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getItems()).isNotNull();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29553})
    @Description("Find Process Groups when shared secret is invalid")
    public void findProcessGroupswithInvalidSharedSecret() {

        ResponseWrapper<ErrorMessage> responseWrapper = processGroupsUtil.findProcessGroups(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, INVALID_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_OR_MISSING_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29555})
    @Description("Get Unauthorized Error when shared secret parameter is not provided")
    public void findProcessGroupWithoutSharedSecretTest() {
        ResponseWrapper<ErrorMessage> responseWrapper = processGroupsUtil.findProcessGroupsWithoutKeyParameter(
            HttpStatusCode.UNAUTHORIZED, ErrorMessage.class, NO_SHARED_SECRET);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getError()).isEqualTo(UNAUTHORIZED_ERROR);
        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).isEqualTo(INVALID_CREDENTIAL_MSG);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29556})
    @Description("Get Not Acceptable error when incorrect Accept Header is provided")
    public void findProcessGroupWithIncorrectAcceptHeader() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(DFSApiEnum.PROCESS_GROUPS, ErrorMessage.class)
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
