package com.apriori.nts.tests;

import com.apriori.nts.enums.NTSAPIEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class Notifications {

    @Test
    @TestRail(testCaseId = {"4530"})
    @Description("Get a list of notifications using the NTS API")
    public void getNotifications() {
        RequestEntity requestEntity = RequestEntityUtil.init(NTSAPIEnum.GET_NOTIFICATIONS, com.apriori.nts.entity.response.Notifications.class);

        Assert.assertEquals(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());
    }
}
