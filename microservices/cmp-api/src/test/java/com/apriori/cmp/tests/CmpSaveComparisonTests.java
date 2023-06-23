package com.apriori.cmp.tests;

import com.apriori.cmp.entity.builder.ComparisonObjectBuilder;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.response.PostComparisonResponse;
import com.apriori.cmp.utils.ComparisonUtils;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CmpSaveComparisonTests {

    ComparisonUtils comparisonUtils = new ComparisonUtils();
    private UserCredentials currentUser;
    private String comparisonName;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"25931"})
    @Description("Create a Comparison via CMP-API")
    public void testCreateComparison() {

        currentUser = UserUtil.getUser();
        comparisonName = new GenerateStringUtil().generateComparisonName();

        ComparisonObjectBuilder baseScenario = ComparisonObjectBuilder.builder()
            .externalIdentity("123456789")
            .position(1)
            .basis(true)
            .build();
        ComparisonObjectBuilder comp1 = ComparisonObjectBuilder.builder()
            .externalIdentity("987654321")
            .position(2)
            .build();
        ComparisonObjectBuilder comp2 = ComparisonObjectBuilder.builder()
            .externalIdentity("147258369")
            .position(3)
            .build();

        List<ComparisonObjectBuilder> compObjs = new ArrayList<ComparisonObjectBuilder>();
        compObjs.add(baseScenario);
        compObjs.add(comp1);
        compObjs.add(comp2);

        CreateComparison comparison = CreateComparison.builder()
            .comparisonName(comparisonName)
            .comparisonType("BASIS")
            .objectType("SCENARIO")
            .objects(compObjs)
            .build();

        ResponseWrapper<PostComparisonResponse> savedComparison = comparisonUtils.createComparison(comparison, currentUser);

        softAssertions.assertThat(savedComparison.getStatusCode()).as("Ensure a 201 response received").isEqualTo(201);
        softAssertions.assertAll();

    }

}
