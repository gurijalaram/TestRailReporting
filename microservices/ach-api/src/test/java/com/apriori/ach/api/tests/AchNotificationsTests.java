package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.CustomerAch;
import com.apriori.ach.api.models.response.Notifications;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
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
    private CustomerAch apInternal;
    private AchTestUtil achTestUtil;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser().useTokenInRequests();
        achTestUtil = new AchTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {21958})
    @Description("Returns the list of the notifications")
    public void getNotifications() {
        apInternal = achTestUtil.getAprioriInternal();
        SoftAssertions soft = new SoftAssertions();

        String customerIdentity = apInternal.getIdentity();
        String deploymentIdentity = apInternal.getDeployments().get(0).getIdentity();
        ResponseWrapper<Notifications> notifications = achTestUtil.getCommonRequest(ACHAPIEnum.NOTIFICATIONS, Notifications.class, HttpStatus.SC_OK, customerIdentity, deploymentIdentity);

        soft.assertThat(notifications.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(0);
        soft.assertAll();
    }
}