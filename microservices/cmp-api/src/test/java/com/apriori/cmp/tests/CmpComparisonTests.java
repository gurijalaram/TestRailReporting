package com.apriori.cmp.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cmp.entity.builder.ComparisonObjectBuilder;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.response.GetComparisonResponse;
import com.apriori.cmp.entity.response.PostComparisonResponse;
import com.apriori.cmp.utils.ComparisonUtils;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CmpComparisonTests {

    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static String componentName1 = "big ring";
    private static String componentName2 = "small ring";
    private static String componentName3 = "Pin";
    private static String componentExt = ".SLDPRT";
    private static File resourceFile1 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName1 + componentExt);
    private static File resourceFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName2 + componentExt);
    private static File resourceFile3 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName3 + componentExt);
    private static ComponentInfoBuilder component1;
    private static ComponentInfoBuilder component2;
    private static ComponentInfoBuilder component3;
    private static String scenarioName;
    private static UserCredentials currentUser;
    private ComparisonUtils comparisonUtils = new ComparisonUtils();
    private String comparisonName;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(testCaseId = "26182")
    @Description("Get a list of comparisons")
    public void getComparisonsTest() {
        currentUser = UserUtil.getUser();

        List<GetComparisonResponse> getComparisonsResponse = comparisonUtils.getComparisons(currentUser);

        assertThat(getComparisonsResponse.size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = "26184")
    @Description("Verify get only shows comparison for a given user")
    public void verifyComparisonForGivenUser() {
        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();
        comparisonName = new GenerateStringUtil().generateComparisonName();

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

        softAssertions.assertAll();
    }
}
