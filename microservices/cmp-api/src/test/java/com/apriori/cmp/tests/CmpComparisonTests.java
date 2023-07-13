package com.apriori.cmp.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cmp.entity.response.GetComparisonResponse;
import com.apriori.cmp.utils.ComparisonUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.List;

public class CmpComparisonTests {

    private ComparisonUtils comparisonUtils = new ComparisonUtils();
    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = "26182")
    @Description("Get a list of comparisons")
    public void getComparisonsTest() {
        currentUser = UserUtil.getUser();

        List<GetComparisonResponse> getComparisonsResponse = comparisonUtils.getComparisons(currentUser);

        assertThat(getComparisonsResponse.size()).isGreaterThan(0);
    }
}
