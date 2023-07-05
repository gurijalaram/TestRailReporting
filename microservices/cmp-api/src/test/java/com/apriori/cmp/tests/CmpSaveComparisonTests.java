package com.apriori.cmp.tests;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cmp.entity.builder.ComparisonObjectBuilder;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.request.UpdateComparison;
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
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CmpSaveComparisonTests {

    private ComparisonUtils comparisonUtils = new ComparisonUtils();
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private UserCredentials currentUser;
    private String comparisonName;
    private String componentName1 = "big ring";
    private String componentName2 = "small ring";
    private String componentName3 = "pin";
    private String componentExt = ".SLDPRT";

    private File resourceFile1 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName1 + componentExt);
    private File resourceFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName2 + componentExt);
    private File resourceFile3 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, componentName3 + componentExt);

    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"25931", "25932"})
    @Description("Create a Comparison via CMP-API")
    public void testCreateThenUpdateComparison() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        ComponentInfoBuilder component1 = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName1)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile1)
                .user(currentUser)
                .build()
        );
        ComponentInfoBuilder component2 = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName2)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile2)
                .user(currentUser)
                .build()
        );
        ComponentInfoBuilder component3 = componentsUtil.postComponentQueryCID(
            ComponentInfoBuilder.builder()
                .componentName(componentName3)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile3)
                .user(currentUser)
                .build()
        );

        currentUser = UserUtil.getUser();
        comparisonName = new GenerateStringUtil().generateComparisonName();
        String updatedComparisonName = new GenerateStringUtil().generateComparisonName();

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

        List<ComparisonObjectBuilder> comparisonObjectsList = new ArrayList<ComparisonObjectBuilder>();
        comparisonObjectsList.add(baseScenario);
        comparisonObjectsList.add(comparisonScenario1);
        comparisonObjectsList.add(comparisonScenario2);

        CreateComparison comparison = CreateComparison.builder()
            .comparisonName(comparisonName)
            .comparisonType("BASIS")
            .comparisonObjectType("SCENARIO")
            .objectsToCompare(comparisonObjectsList)
            .build();

        PostComparisonResponse savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser);

        softAssertions.assertThat(savedComparisonResponse.getComparisonName()).as("Ensure same comparison name returned").isEqualTo(comparisonName);

        GetComparisonResponse getComparisonDetails =comparisonUtils.getComparison(savedComparisonResponse.getIdentity(), currentUser);

        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().size()).as("Verify number of Comparison Objects")
            .isEqualTo(3);
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().get(0).getIdentity()).as("Verify first object is Component 1")
            .isEqualTo(component1.getScenarioIdentity());
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().get(0).getBasis()).as("Verify first object is the basis")
            .isTrue();

        List<ComparisonObjectBuilder> comparisonObjects = comparison.getObjectsToCompare();

        comparisonObjects.get(0).setBasis(false);
        comparisonObjects.get(0).setPosition(2);
        comparisonObjects.get(1).setPosition(3);
        comparisonObjects.get(2).setPosition(1);
        comparisonObjects.get(0).setBasis(true);

        UpdateComparison updateComparison = UpdateComparison.builder()
            .comparisonName(updatedComparisonName)
            .objectsToCompare(comparisonObjects)
            .build();

        GetComparisonResponse updatedComparison = comparisonUtils.updateComparison(getComparisonDetails.getIdentity(), updateComparison, currentUser);

        softAssertions.assertThat(savedComparisonResponse.getComparisonName()).as("Ensure same comparison name returned")
            .isEqualTo(updatedComparisonName);
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().size()).as("Verify number of Comparison Objects")
            .isEqualTo(3);
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().get(0).getIdentity()).as("Verify first object is Component 2")
            .isEqualTo(component1.getScenarioIdentity());
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().get(0).getBasis()).as("Verify first object is the basis")
            .isTrue();

        softAssertions.assertAll();

    }

}
