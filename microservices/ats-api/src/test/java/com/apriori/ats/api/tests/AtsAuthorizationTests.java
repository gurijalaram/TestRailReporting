package com.apriori.ats.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.ats.api.models.response.AuthorizationResponse;
import com.apriori.ats.api.models.response.CloudContextResponse;
import com.apriori.ats.api.utils.AtsUtil;
import com.apriori.ats.api.utils.enums.ATSAPIEnum;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Token;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AtsAuthorizationTests {
    private AtsUtil atsUtil;
    private RequestEntityUtil requestEntityUtil;
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        atsUtil = new AtsUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {3581})
    @Description("Generate a JWT from the ATS Token endpoint")
    public void generateTokenTest() {
        ResponseWrapper<Token> response = atsUtil.getToken();

        assertThat(response.getResponseEntity().getToken(), is(not(emptyString())));
    }

    @Test
    @Tag(API_SANITY)
    @TestRail(id = {3913})
    @Description("Authorize a user to access a specified application")
    public void authorizeUserTest() {
        ResponseWrapper<AuthorizationResponse> response = atsUtil.authorizeUser(SharedCustomerUtil.getAuthTargetCloudContext());

        assertThat(response.getResponseEntity().getEmail(), is(equalTo(requestEntityUtil.getEmbeddedUser().getEmail())));
    }

    @Test
    @TestRail(id = {22088})
    @Description("Get a Cloud Context identified by a cloud context string")
    public void getCloudContextTest() {
        String customerIdentity = SharedCustomerUtil.getCustomerData().getIdentity();
        String contextString = requestEntityUtil.getEmbeddedUser().getUserDetails().getCustomerData().getCloudContext();
        ResponseWrapper<CloudContextResponse> getCloudContext = atsUtil.getCommonRequest(ATSAPIEnum.CLOUD_CONTEXT, CloudContextResponse.class, HttpStatus.SC_OK, contextString);

        soft.assertThat(getCloudContext.getResponseEntity().getCustomerIdentity()).isEqualTo(customerIdentity);
        soft.assertAll();
    }
}
