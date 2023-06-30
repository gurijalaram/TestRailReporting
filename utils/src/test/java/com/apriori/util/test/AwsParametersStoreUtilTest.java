package com.apriori.util.test;

import com.apriori.utils.AwsParametersStoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

@Slf4j
public class AwsParametersStoreUtilTest {

    @Test
    public void testGetSystemParameter() {
        final String apiId = AwsParametersStoreUtil.getSystemParameter("/dev-cis-sbx-apint/cds/api_id");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(apiId).isNotEmpty();
        softAssertions.assertAll();
    }
}
