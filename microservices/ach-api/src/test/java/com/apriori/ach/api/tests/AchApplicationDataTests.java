package com.apriori.ach.api.tests;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.ApplicationMetadata;
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
public class AchApplicationDataTests extends AchTestUtil {

    @Test
    @TestRail(id = {21957})
    @Description("Returns the current application's metadata.")
    public void getAppMetadata() {
        SoftAssertions soft = new SoftAssertions();
        ResponseWrapper<ApplicationMetadata> appMetadata = getCommonRequest(ACHAPIEnum.APP_METADATA, ApplicationMetadata.class, HttpStatus.SC_OK);

        soft.assertThat(appMetadata.getResponseEntity().getApplicationName()).isEqualTo("aPriori Cloud Home");
        soft.assertAll();
    }
}