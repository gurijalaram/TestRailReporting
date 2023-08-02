package com.apriori.ach.tests;

import com.apriori.ach.enums.ACHAPIEnum;
import com.apriori.ach.models.response.PeopleItems;
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

public class AchPeopleTests {
    private AchTestUtil achTestUtil = new AchTestUtil();

    @BeforeEach
    public void getToken() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
    }

    @Test
    @TestRail(id = {21954})
    @Description("Returns a list of user references")
    public void getPeople() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<PeopleItems> people = achTestUtil.getCommonRequest(ACHAPIEnum.PEOPLE, PeopleItems.class, HttpStatus.SC_OK);

        soft.assertThat(people.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(people.getResponseEntity().getItems().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }
}