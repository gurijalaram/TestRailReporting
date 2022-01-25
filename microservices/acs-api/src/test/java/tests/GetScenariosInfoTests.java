package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoItem;
import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.entity.response.upload.FileResponse;
import com.apriori.entity.response.upload.FileUploadOutputs;
import com.apriori.entity.response.upload.ScenarioIterationKey;
import com.apriori.entity.response.upload.ScenarioKey;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetScenariosInfoTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "9597")
    @Description("Validate Get Scenarios Info - Two Parts")
    public void testGetScenariosInfoTwoParts() {
        List<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt"));

        Map<GetScenariosInfoItem, ScenarioKey> scenarioItemsResponse = coreGetScenariosInfoTest(fileNames);

        getScenariosInfoAssertions(scenarioItemsResponse, fileNames);
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10384")
    @Description("Validate Get Scenarios Info - Four Parts")
    public void testGetScenariosInfoFourParts() {
        List<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt", "bracket_basic.prt", "flanged_hole.prt"));

        Map<GetScenariosInfoItem, ScenarioKey> scenarioItemsResponse = coreGetScenariosInfoTest(fileNames);

        getScenariosInfoAssertions(scenarioItemsResponse, fileNames);
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10182")
    @Description("Negative Get Scenarios Info - Invalid Iteration Identities")
    public void negativeGetScenariosInfoInvalidIterationIdentitiesTest() {
        List<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt"));
        List<FileUploadOutputs> fileUploadOutputsArrayList = fileUpload(fileNames);

        ScenarioIterationKey keyOne = fileUploadOutputsArrayList.get(0).getScenarioIterationKey();
        ScenarioIterationKey keyTwo = fileUploadOutputsArrayList.get(1).getScenarioIterationKey();

        keyOne.setIteration(11);
        keyTwo.setIteration(12);

        AcsResources acsResources = new AcsResources();

        ResponseWrapper<GetScenariosInfoResponse> response = acsResources.getScenariosInformation(
                keyOne,
                keyTwo
        );

        assertThat(response.getResponseEntity().isEmpty(), is(equalTo(true)));
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10203")
    @Description("Negative Get Scenarios Info - Empty Body")
    public void negativeGetScenariosInfoEmptyBodyTest() {
        AcsResources acsResources = new AcsResources();

        ResponseWrapper<GetScenariosInfoResponse> response = acsResources.getScenariosInfoNullBody();

        assertThat(response.getStatusCode(), is(equalTo(400)));
        assertThat(response.getBody().contains("The request should not be null"), is(equalTo(true)));
    }

    /**
     * File upload part of get scenarios info test
     *
     * @param fileNames output from previous part of test
     * @return List of FileUploadOutputs - data to assert on
     */
    private List<FileUploadOutputs> fileUpload(List<String> fileNames) {
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        FileUploadResources fileUploadResources = new FileUploadResources();

        List<FileResponse> fileResponses = new ArrayList<>();
        List<FileUploadOutputs> fileUploadOutputs = new ArrayList<>();
        String processGroup = "";

        for (int i = 0; i < fileNames.size(); i++) {
            processGroup = i == 0 ? ProcessGroupEnum.CASTING.getProcessGroup() : ProcessGroupEnum.SHEET_METAL.getProcessGroup();
            fileResponses.add(fileUploadResources.initialisePartUpload(
                    fileNames.get(i),
                    processGroup
            ));

            fileUploadOutputs.add(fileUploadResources.createFileUploadWorkorderSuppressError(fileResponses.get(i), testScenarioName));
        }

        return fileUploadOutputs;
    }

    /**
     * Main part of get scenarios info test
     *
     * @param fileNames - List of Strings - file names to upload
     * @return Map of GetScenariosInfoItem and ScenarioKey
     */
    private Map<GetScenariosInfoItem, ScenarioKey> coreGetScenariosInfoTest(List<String> fileNames) {
        List<FileUploadOutputs> fileUploadOutputs = fileUpload(fileNames);

        List<ScenarioIterationKey> scenarioIterationKeys = new ArrayList<>();

        for (FileUploadOutputs fileUploadOutput : fileUploadOutputs) {
            scenarioIterationKeys.add(fileUploadOutput.getScenarioIterationKey());
        }

        AcsResources acsResources = new AcsResources();
        ResponseWrapper<GetScenariosInfoResponse> response = acsResources.getScenariosInformationOneScenario(scenarioIterationKeys);

        List<GetScenariosInfoItem> getScenariosInfoItems = new ArrayList<>();
        List<ScenarioKey> scenarioKeys = new ArrayList<>();
        Map<GetScenariosInfoItem, ScenarioKey> responseHashMap = new HashMap<>();

        for (int j = 0; j < response.getResponseEntity().size(); j++) {
            getScenariosInfoItems.add(response.getResponseEntity().get(j));
            scenarioKeys.add(getScenariosInfoItems.get(j).getScenarioIterationKey().getScenarioKey());
            responseHashMap.put(getScenariosInfoItems.get(j), scenarioKeys.get(j));
        }

        return responseHashMap;
    }

    /**
     * Performs assertions on response from get scenarios info endpoint
     *
     * @param scenarioItemsResponse - Map of get scenarios info items and scenario keys to allow asserts
     * @param fileNames - file names to assert against
     */
    private void getScenariosInfoAssertions(Map<GetScenariosInfoItem, ScenarioKey> scenarioItemsResponse, List<String> fileNames) {

        List<GetScenariosInfoItem> getScenariosInfoItems = new ArrayList<>(scenarioItemsResponse.keySet());
        List<ScenarioKey> scenarioKeys = new ArrayList<>(scenarioItemsResponse.values());

        String userToExpect = "qa-automation-01";
        String componentTypeToExpect = "PART";
        String typeNameToExpect = "partState";

        SoftAssertions softAssertions = new SoftAssertions();

        for (int i = 0; i < scenarioItemsResponse.size(); i++) {
            GetScenariosInfoItem scenariosInfoItem = getScenariosInfoItems.get(i);

            softAssertions.assertThat(!scenariosInfoItem.getInitialized());
            softAssertions.assertThat(!scenariosInfoItem.getMissing());
            softAssertions.assertThat(!scenariosInfoItem.getVirtual());
            softAssertions.assertThat(!scenariosInfoItem.getLocked());

            softAssertions.assertThat(scenariosInfoItem.getCreatedBy().equals(userToExpect));
            softAssertions.assertThat(scenariosInfoItem.getUpdatedBy().equals(userToExpect));

            softAssertions.assertThat(scenariosInfoItem.getComponentType().equals(componentTypeToExpect));
            softAssertions.assertThat(scenariosInfoItem.getFileName().equals(fileNames.get(i).toLowerCase()));

            softAssertions.assertThat(scenarioKeys.get(i).getTypeName().equals(typeNameToExpect));
        }

        softAssertions.assertAll();
    }
}
