package com.apriori.ach.tests;

import com.apriori.ach.entity.response.CustomerAch;
import com.apriori.ach.entity.response.Notifications;
import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class AchNotificationsTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private CustomerAch apInternal;

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        apInternal = achTestUtil.getAprioriInternal();
    }

    @Test
    @TestRail(testCaseId = {"21958"})
    @Description("Returns the list of the notifications")
    public void getNotifications() {
        SoftAssertions soft = new SoftAssertions();
        String customerIdentity = apInternal.getIdentity();
        String deploymentIdentity = apInternal.getDeployments().get(0).getIdentity();
        ResponseWrapper<Notifications> notifications = achTestUtil.getCommonRequest(ACHAPIEnum.NOTIFICATIONS, Notifications.class, HttpStatus.SC_OK, customerIdentity, deploymentIdentity);

        soft.assertThat(notifications.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();
    }
}