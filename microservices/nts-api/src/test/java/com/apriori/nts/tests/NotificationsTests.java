package com.apriori.nts.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.nts.entity.response.Notifications;
import com.apriori.nts.enums.NTSAPIEnum;
import com.apriori.utils.TestHelper;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class NotificationsTests extends TestHelper {

    @Test
    @TestRail(testCaseId = {"4530"})
    @Description("Get a list of notifications using the NTS API")
    public void getNotifications() {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_NOTIFICATIONS,
            Notifications.class);

        ResponseWrapper<Notifications> notificationResponse = HTTPRequest.build(requestEntity).get();

        Assert.assertEquals(HttpStatus.SC_OK, notificationResponse.getStatusCode());
        assertThat(notificationResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}
