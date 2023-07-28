package com.apriori;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cmp.entity.builder.ComparisonObjectBuilder;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.request.UpdateComparison;
import com.apriori.cmp.entity.response.ErrorResponse;
import com.apriori.cmp.entity.response.GetComparisonResponse;
import com.apriori.cmp.entity.response.PostComparisonResponse;
import com.apriori.cmp.utils.ComparisonUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CmpSaveComparisonTests {

    private static ComponentsUtil componentsUtil = new ComponentsUtil();
    private static UserCredentials currentUser;
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
    private ComparisonUtils comparisonUtils = new ComparisonUtils();
    private String comparisonName;
    private SoftAssertions softAssertions = new SoftAssertions();

    @BeforeClass
    public static void prepScenarios() {
        scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

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
    }

    @Test
    @TestRail(id = {25931, 25932})
    @Description("Create a Comparison via CMP-API")
    public void testCreateThenUpdateComparison() {

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

        softAssertions.assertThat(savedComparisonResponse.getComparisonName()).as("Ensure same comparison name returned").isEqualTo(comparisonName);

        GetComparisonResponse getComparisonDetails = comparisonUtils.getComparison(currentUser, savedComparisonResponse.getIdentity());

        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().size()).as("Verify number of Comparison Objects")
            .isEqualTo(3);
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().get(0).getExternalIdentity()).as("Verify first object is Component 1")
            .isEqualTo(component1.getScenarioIdentity());
        softAssertions.assertThat(getComparisonDetails.getObjectsToCompare().get(0).getBasis()).as("Verify first object is the basis")
            .isTrue();

        List<ComparisonObjectBuilder> comparisonObjects = comparison.getObjectsToCompare();

        comparisonObjects.get(0).setBasis(false);
        comparisonObjects.get(0).setPosition(2);
        comparisonObjects.get(1).setPosition(3);
        comparisonObjects.get(2).setPosition(1);
        comparisonObjects.get(2).setBasis(true);

        UpdateComparison updateComparison = UpdateComparison.builder()
            .comparisonName(updatedComparisonName)
            .objectsToCompare(comparisonObjects)
            .build();

        GetComparisonResponse updatedComparison = comparisonUtils.updateComparison(
            updateComparison, currentUser, GetComparisonResponse.class, HttpStatus.SC_OK, getComparisonDetails.getIdentity());

        softAssertions.assertThat(updatedComparison.getComparisonName()).as("Ensure same comparison name returned")
            .isEqualTo(updatedComparisonName);
        softAssertions.assertThat(updatedComparison.getObjectsToCompare().size()).as("Verify number of Comparison Objects")
            .isEqualTo(3);
        softAssertions.assertThat(updatedComparison.getObjectsToCompare().get(0).getExternalIdentity()).as("Verify first object is Component 2")
            .isEqualTo(component3.getScenarioIdentity());
        softAssertions.assertThat(updatedComparison.getObjectsToCompare().get(0).getBasis()).as("Verify first object is the basis")
            .isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {25934, 25935, 25936, 25938, 25940, 25942, 25952, 25953})
    @Description("Test Request Validation for Create Comparison Endpoint")
    public void testCreateComparisonValidation() {

        comparisonName = new GenerateStringUtil().generateComparisonName();

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
            .comparisonName(null)
            .comparisonType("BASIS")
            .comparisonObjectType("SCENARIO")
            .objectsToCompare(comparisonObjectsList)
            .build();

        ErrorResponse savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for null comparison name")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for null comparison name")
            .isEqualTo("'comparisonName' should not be null.");

        comparison.setComparisonName("");
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for empty string comparison name")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for empty string comparison name")
            .isEqualTo("'comparisonName' should not be null.");

        comparison.setComparisonName("Neque porro quisquam est qui dolorem ipsum quia dolor sit amet consectetur adipisci velit");
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for over-length comparison name")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for over-length comparison name")
            .isEqualTo("'comparisonName' should not be more than 64 characters.");

        comparison.setComparisonName(comparisonName);
        comparison.setComparisonType("BASED");
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for invalid comparison type")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for invalid comparison type")
            .isEqualTo("'comparisonType' should not be null.");

        comparison.setComparisonType("BASIS");
        comparison.setComparisonObjectType("ASSEMBLY");
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for invalid object type")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for invalid object type")
            .isEqualTo("'objectType' should not be null.");

        comparison.setComparisonObjectType("SCENARIO");
        comparison.getObjectsToCompare().get(1).setBasis(true);
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for multiple bases set")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for multiple bases set")
            .isEqualTo("'Contains a single basis' should be true.");

        comparison.getObjectsToCompare().get(1).setBasis(false);
        comparison.getObjectsToCompare().get(2).setPosition(2);
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for duplicate position values")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for duplicate position values")
            .isEqualTo("'Has consecutive, unique object positions' should be true.");

        comparison.getObjectsToCompare().get(2).setPosition(3);
        comparison.getObjectsToCompare().get(0).setBasis(false);
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for no basis set")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for no basis set")
            .isEqualTo("'Contains a single basis' should be true.");

        comparison.getObjectsToCompare().get(0).setBasis(true);
        comparison.getObjectsToCompare().get(0).setPosition(0);
        comparison.getObjectsToCompare().get(1).setPosition(1);
        comparison.getObjectsToCompare().get(2).setPosition(2);
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for 0-indexed positions")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for 0-indexed positions")
            .isEqualTo("2 validation failures were found:* 'First object position' should be equal to 1.* 'Has consecutive, unique object positions' should be true.");

        comparison.getObjectsToCompare().get(0).setPosition(2);
        comparison.getObjectsToCompare().get(1).setPosition(3);
        comparison.getObjectsToCompare().get(2).setPosition(4);
        savedComparisonResponse = comparisonUtils.createComparison(comparison, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getStatus()).as("Verify 400 error returned for 2-indexed positions")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(savedComparisonResponse.getMessage()).as("Verify error message for 2-indexed positions")
            .isEqualTo("2 validation failures were found:* 'First object position' should be equal to 1.* 'Has consecutive, unique object positions' should be true.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {25943, 25945, 25946, 25947, 25948, 25949, 25950})
    @Description("Test Request Validation for Create Comparison Endpoint")
    public void testUpdateComparisonValidation() {
        comparisonName = new GenerateStringUtil().generateComparisonName();

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

        UpdateComparison comparisonUpdates = UpdateComparison.builder()
            .comparisonName("Neque porro quisquam est qui dolorem ipsum quia dolor sit amet consectetur adipisci velit")
            .objectsToCompare(comparisonObjectsList)
            .build();

        ErrorResponse updatedComparison = comparisonUtils.updateComparison(
            comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());

        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned for over-length comparison name")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for over-length comparison name")
            .isEqualTo("'comparisonName' should not be more than 64 characters.");

        comparisonUpdates.setComparisonName(comparisonName);
        comparisonUpdates.getObjectsToCompare().get(0).setBasis(null);
        comparisonUpdates.getObjectsToCompare().get(1).setBasis(null);
        comparisonUpdates.getObjectsToCompare().get(2).setBasis(null);

        updatedComparison = comparisonUtils.updateComparison(comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());
        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned when no basis set")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for no basis set")
            .isEqualTo("'Contains a single basis' should be true.");

        comparisonUpdates.getObjectsToCompare().get(0).setBasis(true);
        comparisonUpdates.getObjectsToCompare().get(1).setBasis(true);
        comparisonUpdates.getObjectsToCompare().get(2).setBasis(true);

        updatedComparison = comparisonUtils.updateComparison(comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());
        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned when all objects set as basis")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for all objects set as basis")
            .isEqualTo("'Contains a single basis' should be true.");

        comparisonUpdates.getObjectsToCompare().get(0).setBasis(false);
        comparisonUpdates.getObjectsToCompare().get(1).setBasis(false);
        comparisonUpdates.getObjectsToCompare().get(2).setBasis(false);

        updatedComparison = comparisonUtils.updateComparison(comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());
        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned when all objects set as not basis")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for all objects set as not basis")
            .isEqualTo("'Contains a single basis' should be true.");

        comparisonUpdates.getObjectsToCompare().get(0).setBasis(true);
        comparisonUpdates.getObjectsToCompare().get(1).setBasis(false);
        comparisonUpdates.getObjectsToCompare().get(2).setBasis(false);

        comparisonUpdates.getObjectsToCompare().get(1).setPosition(1);
        updatedComparison = comparisonUtils.updateComparison(comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());
        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned when duplicate positions provided")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for duplicate positions provided")
            .isEqualTo("'Has consecutive, unique object positions' should be true.");

        comparisonUpdates.getObjectsToCompare().get(0).setPosition(0);
        comparisonUpdates.getObjectsToCompare().get(1).setPosition(1);
        comparisonUpdates.getObjectsToCompare().get(2).setPosition(2);
        updatedComparison = comparisonUtils.updateComparison(comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());
        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned when 0-indexed positions used")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for 0-indexed positions used")
            .isEqualTo("2 validation failures were found:* 'First object position' should be equal to 1.* 'Has consecutive, unique object positions' should be true.");

        comparisonUpdates.getObjectsToCompare().get(0).setPosition(2);
        comparisonUpdates.getObjectsToCompare().get(1).setPosition(3);
        comparisonUpdates.getObjectsToCompare().get(2).setPosition(4);
        updatedComparison = comparisonUtils.updateComparison(comparisonUpdates, currentUser, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST, savedComparisonResponse.getIdentity());
        softAssertions.assertThat(updatedComparison.getStatus()).as("Verify 400 error returned when 2-indexed positions used")
            .isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(updatedComparison.getMessage()).as("Verify error message for 2-indexed positions used")
            .isEqualTo("2 validation failures were found:* 'First object position' should be equal to 1.* 'Has consecutive, unique object positions' should be true.");

        softAssertions.assertAll();
    }

}
