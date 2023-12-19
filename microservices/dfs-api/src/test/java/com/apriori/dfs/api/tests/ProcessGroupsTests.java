package com.apriori.dfs.api.tests;

import com.apriori.dfs.api.models.response.DigitalFactory;
import com.apriori.dfs.api.models.response.ProcessGroup;
import com.apriori.dfs.api.models.utils.DigitalFactoryUtil;
import com.apriori.dfs.api.models.utils.ProcessGroupsUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.http.HttpStatusCode;

@ExtendWith(TestRulesAPI.class)
public class ProcessGroupsTests {

    private static final String BAD_REQUEST_ERROR = "Bad Request";
    private static final String PG_NAME = "Stock Machining";
    private static final String IDENTITY_IS_NOT_A_VALID_IDENTITY_MSG = "'identity' is not a valid identity.";
    private static final String INVALID_CONTENT_TYPE = "application/text";
    private static final String INVALID_CREDENTIAL_MSG = "Invalid credential";
    private static final String INVALID_DIGITAL_FACTORY_ID = "1234567890";
    private static final String INVALID_OR_MISSING_CREDENTIAL_MSG = "Invalid or missing credential";
    private static final String INVALID_SHARED_SECRET = "InvalidSharedSecret";
    private static final String NO_SHARED_SECRET = "";
    private static final String VALID_PROCESS_GROUP_ID = "7EU8N44NCK0F";
    private static final String UNAUTHORIZED_ERROR = "Unauthorized";

    private final SoftAssertions softAssertions = new SoftAssertions();

    private final ProcessGroupsUtil processGroupsUtil = new ProcessGroupsUtil();

    @Test
    @TestRail(id = {28959})
    @Description("Gets a process group by identity when shared secret/identity are valid")
    public void getProcessGroupByIdentityTest() {

        ResponseWrapper<ProcessGroup> responseWrapper = processGroupsUtil.getProcessGroup(
            HttpStatusCode.OK, ProcessGroup.class, VALID_PROCESS_GROUP_ID);

        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isNotNull();
        softAssertions.assertThat(responseWrapper.getResponseEntity().getName()).isEqualTo(PG_NAME);
        softAssertions.assertAll();
    }
}
