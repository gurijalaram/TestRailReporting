package com.apriori.cmp.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cmp.api.models.builder.ComparisonObjectBuilder;
import com.apriori.cmp.api.models.request.CreateComparison;
import com.apriori.cmp.api.models.response.GetComparisonResponse;
import com.apriori.cmp.api.models.response.PostComparisonResponse;
import com.apriori.cmp.api.utils.ComparisonUtils;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import com.google.common.collect.Comparators;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CmpComparisonTests {

    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static ComponentInfoBuilder component1;
    private static ComponentInfoBuilder component2;
    private static ComponentInfoBuilder component3;
    private static String scenarioName;
    private static UserCredentials currentUser;
    private ComparisonUtils comparisonUtils = new ComparisonUtils();
    private String comparisonName;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Tag(API_SANITY)
    @TestRail(id = 26182)
    @Description("Get a list of comparisons")
    public void getComparisonsTest() {
        currentUser = UserCredentials.init("qa-automation-01@apriori.com", "TrumpetSnakeFridgeToasty18!%");

        List<GetComparisonResponse> getComparisonsResponse = comparisonUtils.getComparisons(currentUser);

        assertThat(getComparisonsResponse.size(), Matchers.greaterThan(0));
    }

    @Test
    @TestRail(id = {26152, 26183, 26184, 26185, 26186, 26187})
    @Description("Verify comparison for a given user")
    public void verifyComparisonForGivenUser() {
        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();
        comparisonName = new GenerateStringUtil().generateComparisonName();

        String componentName1 = "big ring";
        String componentName2 = "small ring";
        String componentName3 = "Pin";
        String componentExt = ".SLDPRT";
        File resourceFile1 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName1 + componentExt);
        File resourceFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName2 + componentExt);
        File resourceFile3 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName3 + componentExt);

        component1 = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName1)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile1)
                .user(currentUser)
                .build()
        );
        component2 = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName2)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile2)
                .user(currentUser)
                .build()
        );
        component3 = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName3)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile3)
                .user(currentUser)
                .build()
        );

        ComparisonObjectBuilder baseScenario = ComparisonObjectBuilder.builder()
            .externalIdentity(component1.getScenarioIdentity())
            .position(1)
            .basis(true)
            .build();
        ComparisonObjectBuilder comparisonScenario1 = ComparisonObjectBuilder.builder()
            .externalIdentity(component2.getScenarioIdentity())
            .position(2)
            .build();
        ComparisonObjectBuilder comparisonScenario2 = ComparisonObjectBuilder.builder()
            .externalIdentity(component3.getScenarioIdentity())
            .position(3)
            .build();

        List<ComparisonObjectBuilder> comparisonObjectsList = new ArrayList<>();
        comparisonObjectsList.add(baseScenario);
        comparisonObjectsList.add(comparisonScenario1);
        comparisonObjectsList.add(comparisonScenario2);

        CreateComparison comparison = CreateComparison.builder()
            .comparisonName(comparisonName)
            .comparisonType("BASIS")
            .comparisonObjectType("SCENARIO")
            .objectsToCompare(comparisonObjectsList)
            .build();

        PostComparisonResponse savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, PostComparisonResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(savedComparisonResponse.getComparisonName()).isEqualTo(comparisonName);

        List<GetComparisonResponse> comparisonsResponse = comparisonUtils.queryComparison(currentUser, "pageNumber, 1", "pageSize, 10", "createdBy[EQ],"
            + savedComparisonResponse.getCreatedBy());

        comparisonsResponse.forEach(comparisonResponse -> softAssertions.assertThat(comparisonResponse.getCreatedBy()).isEqualTo(savedComparisonResponse.getCreatedBy()));

        List<GetComparisonResponse> sortResponse = comparisonUtils.queryComparison(currentUser, "pageNumber, 1", "pageSize, 2000", "sortBy[DESC],"
            + "createdAt");

        softAssertions.assertThat(Comparators.isInOrder(sortResponse.stream().map(GetComparisonResponse::getCreatedAt).collect(Collectors.toList()), Comparator.reverseOrder())).isTrue();

        final String secondUser = comparisonUtils.getCurrentPerson(UserUtil.getUser()).getIdentity();

        List<GetComparisonResponse> secondUserQuery = comparisonUtils.queryComparison(currentUser, "pageNumber, 1", "pageSize, 10", "createdBy[EQ],"
            + secondUser);

        secondUserQuery.forEach(userQuery -> softAssertions.assertThat(userQuery.getCreatedBy()).isNotEqualTo(savedComparisonResponse.getCreatedBy()));

        ResponseWrapper<String> deleteResponse = comparisonUtils.deleteComparison(savedComparisonResponse.getIdentity(), currentUser);

        softAssertions.assertThat(deleteResponse.getBody()).isEmpty();

        softAssertions.assertAll();
    }
}