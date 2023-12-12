package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.PeopleItems;
import com.apriori.ach.api.utils.AchTestUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AchPeopleTests extends AchTestUtil {

    @Test
    @TestRail(id = {21954})
    @Description("Returns a list of user references")
    public void getPeople() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<PeopleItems> people = getCommonRequest(ACHAPIEnum.PEOPLE, PeopleItems.class, HttpStatus.SC_OK);

        soft.assertThat(people.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(people.getResponseEntity().getItems().get(0).getIdentity()).isNotEmpty();
        soft.assertAll();
    }
}