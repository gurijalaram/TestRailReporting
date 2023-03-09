package com.apriori.ach.tests;

import com.apriori.ach.entity.response.PeopleItems;
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

public class AchPeopleTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @Before
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(testCaseId = {"21954"})
    @Description("Returns a list of user references")
    public void getPeople() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<PeopleItems> people = achTestUtil.getCommonRequest(ACHAPIEnum.PEOPLE, PeopleItems.class, HttpStatus.SC_OK);

        soft.assertThat(people.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(people.getResponseEntity().getItems().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }
}