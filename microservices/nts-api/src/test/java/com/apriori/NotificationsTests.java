package com.apriori;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.nts.entity.response.Notifications;
import com.apriori.nts.enums.NTSAPIEnum;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

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
