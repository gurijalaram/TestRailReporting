package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.CustomerAch;
import com.apriori.ach.api.models.response.Notifications;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchNotificationsTests {
    private AchTestUtil achTestUtil = new AchTestUtil();
    private final UserCredentials currentUser = UserUtil.getUser();
    private CustomerAch apInternal;

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
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