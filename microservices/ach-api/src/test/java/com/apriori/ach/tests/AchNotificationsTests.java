package com.apriori.ach.tests;

import com.apriori.ach.entity.response.CustomerAch;
import com.apriori.ach.entity.response.Notifications;
import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.utils.AchTestUtil;
import com.apriori.authorization.AuthorizationUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AchNotificationsTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private CustomerAch apInternal;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        apInternal = achTestUtil.getAprioriInternal();
    }

    @Test
    @TestRail(id = {21958})
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