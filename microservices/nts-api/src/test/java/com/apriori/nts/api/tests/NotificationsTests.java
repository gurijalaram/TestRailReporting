package com.apriori.nts.api.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.nts.api.enums.NTSAPIEnum;
import com.apriori.nts.api.models.response.Notifications;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class NotificationsTests {

    @Test
    @TestRail(id = {4530})
    @Description("Get a list of notifications using the NTS API")
    public void getNotifications() {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_NOTIFICATIONS,
                Notifications.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Notifications> notificationResponse = HTTPRequest.build(requestEntity).get();

        assertThat(notificationResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}
