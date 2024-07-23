package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.AchErrorResponse;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Deployments;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchDeploymentsTest {
    private SoftAssertions soft = new SoftAssertions();

    private AchTestUtil achTestUtil;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser().useTokenInRequests();
        achTestUtil = new AchTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {29282})
    @Description("Get full list of deployments for a customer")
    public void getDeployments() {
        String customerIdentity = achTestUtil.getAprioriInternal().getIdentity();
        Deployments customerDeployments = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_DEPLOYMENTS, Deployments.class, HttpStatus.SC_OK, customerIdentity).getResponseEntity();

        soft.assertThat(customerDeployments.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {29283})
    @Description("Get deployments with different customer identity than the current user")
    public void getDifferentCustomerDeployments() {
        String customerIdentity = "52IC4D3J98GN";
        AchErrorResponse errorResponse = achTestUtil.getCommonRequest(ACHAPIEnum.CUSTOMER_DEPLOYMENTS, AchErrorResponse.class, HttpStatus.SC_FORBIDDEN, customerIdentity).getResponseEntity();

        soft.assertThat(errorResponse.getMessage()).isEqualTo("Cross customer request attempt!");
        soft.assertAll();
    }
}