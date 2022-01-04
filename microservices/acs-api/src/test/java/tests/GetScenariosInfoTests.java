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
import java.util.Map;

public class GetScenariosInfoTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "9597")
    @Description("Validate Get Scenarios Info - Two Parts")
    public void testGetScenariosInfoTwoParts() {
        ArrayList<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt"));

        Map<GetScenariosInfoItem, ScenarioKey> scenarioItemsResponse =
                coreGetScenariosInfoTest(fileNames);

        ArrayList<GetScenariosInfoItem> items = new ArrayList<>(scenarioItemsResponse.keySet());
        ArrayList<ScenarioKey> items2 = new ArrayList<>(scenarioItemsResponse.values());

        String userToExpect = "qa-automation-01";
        String componentTypeToExpect = "PART";
        String typeNameToExpect = "partState";

        SoftAssertions softAssertions = new SoftAssertions();

        for (int i = 0; i < scenarioItemsResponse.size(); i++) {
            softAssertions.assertThat(!items.get(i).getInitialized());
            softAssertions.assertThat(!items.get(i).getMissing());
            softAssertions.assertThat(!items.get(i).getVirtual());
            softAssertions.assertThat(!items.get(i).getLocked());

            softAssertions.assertThat(items.get(i).getCreatedBy().equals(userToExpect));
            softAssertions.assertThat(items.get(i).getUpdatedBy().equals(userToExpect));

            softAssertions.assertThat(items.get(i).getComponentType().equals(componentTypeToExpect));
            softAssertions.assertThat(items.get(i).getFileName().equals(fileNames.get(0).toLowerCase()));

            softAssertions.assertThat(items2.get(i).getTypeName().equals(typeNameToExpect));
        }

        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10384")
    @Description("Validate Get Scenarios Info - Four Parts")
    public void testGetScenariosInfoFourParts() {
        ArrayList<String> fileNames = new ArrayList<>(
                Arrays.asList("Casting.prt", "tab_forms.prt", "bracket_basic.prt", "flanged_hole.prt"));

        Map<GetScenariosInfoItem, ScenarioKey> scenarioItemsResponse =
                coreGetScenariosInfoTest(fileNames);

        ArrayList<GetScenariosInfoItem> items = new ArrayList<>(scenarioItemsResponse.keySet());
        ArrayList<ScenarioKey> items2 = new ArrayList<>(scenarioItemsResponse.values());

        String userToExpect = "qa-automation-01";
        String componentTypeToExpect = "PART";
        String typeNameToExpect = "partState";

        SoftAssertions softAssertions = new SoftAssertions();

        for (int i = 0; i < scenarioItemsResponse.size(); i++) {
            softAssertions.assertThat(!items.get(i).getInitialized());
            softAssertions.assertThat(!items.get(i).getMissing());
            softAssertions.assertThat(!items.get(i).getVirtual());
            softAssertions.assertThat(!items.get(i).getLocked());

            softAssertions.assertThat(items.get(i).getCreatedBy().equals(userToExpect));
            softAssertions.assertThat(items.get(i).getUpdatedBy().equals(userToExpect));

            softAssertions.assertThat(items.get(i).getComponentType().equals(componentTypeToExpect));
            softAssertions.assertThat(items.get(i).getFileName().equals(fileNames.get(0).toLowerCase()));

            softAssertions.assertThat(items2.get(i).getTypeName().equals(typeNameToExpect));
        }

        softAssertions.assertAll();
    }

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "10182")
    @Description("Negative Get Scenarios Info - Invalid Iteration Identities")
    public void negativeGetScenariosInfoInvalidIterationIdentitiesTest() {
        ArrayList<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt"));
        ArrayList<FileUploadOutputs> fileUploadOutputsArrayList = fileUpload(fileNames);

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
     * @return ArrayList of FileUploadOutputs - data to assert on
     */
    private ArrayList<FileUploadOutputs> fileUpload(ArrayList<String> fileNames) {
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        FileUploadResources fileUploadResources = new FileUploadResources();

        ArrayList<FileResponse> fileResponses = new ArrayList<>();
        ArrayList<FileUploadOutputs> fileUploadOutputs = new ArrayList<>();
        String processGroup = "";
        int i = 0;

        for (String fileName : fileNames) {
            processGroup = i == 0 ? ProcessGroupEnum.CASTING.getProcessGroup()
                    : ProcessGroupEnum.SHEET_METAL.getProcessGroup();
            fileResponses.add(fileUploadResources.initialisePartUpload(
                    fileName,
                    processGroup
            ));

            fileUploadOutputs.add(
                    fileUploadResources.createFileUploadWorkorderSuppressError(fileResponses.get(i), testScenarioName));

            i++;
        }

        return fileUploadOutputs;
    }

    /**
     * Main part of get scenarios info test
     *
     * @param fileNames - ArrayList of Strings - file names to upload
     * @return Map of GetScenariosInfoItem and ScenarioKey
     */
    private Map<GetScenariosInfoItem, ScenarioKey> coreGetScenariosInfoTest(ArrayList<String> fileNames) {
        ArrayList<FileUploadOutputs> fileUploadOutputs = fileUpload(fileNames);

        ArrayList<ScenarioIterationKey> scenarioIterationKeys = new ArrayList<>();

        for (FileUploadOutputs fileUploadOutput : fileUploadOutputs) {
            scenarioIterationKeys.add(fileUploadOutput.getScenarioIterationKey());
        }

        AcsResources acsResources = new AcsResources();
        ResponseWrapper<GetScenariosInfoResponse> response = acsResources
                .getScenariosInformation2(scenarioIterationKeys);

        ArrayList<GetScenariosInfoItem> getScenariosInfoItems = new ArrayList<>();
        ArrayList<ScenarioKey> scenarioKeys = new ArrayList<>();
        Map<GetScenariosInfoItem, ScenarioKey> responseHashMap = new HashMap<>();

        for (int j = 0; j < response.getResponseEntity().size(); j++) {
            getScenariosInfoItems.add(response.getResponseEntity().get(j));
            scenarioKeys.add(getScenariosInfoItems.get(j).getScenarioIterationKey().getScenarioKey());
            responseHashMap.put(getScenariosInfoItems.get(j), scenarioKeys.get(j));
        }

        return responseHashMap;
    }

}
