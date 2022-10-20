package tests.acs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.scenariosinfo.ScenariosInfoItem;
import com.apriori.acs.entity.response.acs.scenariosinfo.ScenariosInfoResponse;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.entity.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.acs.utils.workorders.FileUploadResources;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.fms.entity.response.FileResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenariosInfoTests extends TestUtil {

    @Test
    @TestRail(testCaseId = "9597")
    @Description("Validate Get Scenarios Info - Two Parts")
    public void testGetScenariosInfoTwoParts() {
        List<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt"));

        List<ScenariosInfoItem> scenarioItemsResponse = coreGetScenariosInfoTest(fileNames);

        getScenariosInfoAssertions(scenarioItemsResponse, fileNames);
    }

    @Test
    @TestRail(testCaseId = "10384")
    @Description("Validate Get Scenarios Info - Four Parts")
    public void testGetScenariosInfoFourParts() {
        List<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt", "bracket_basic.prt", "flanged_hole.prt"));

        List<ScenariosInfoItem> scenarioItemsResponse = coreGetScenariosInfoTest(fileNames);

        getScenariosInfoAssertions(scenarioItemsResponse, fileNames);
    }

    @Test
    @TestRail(testCaseId = "10182")
    @Description("Negative Get Scenarios Info - Invalid Iteration Identities")
    public void negativeGetScenariosInfoInvalidIterationIdentitiesTest() {
        List<String> fileNames = new ArrayList<>(Arrays.asList("Casting.prt", "tab_forms.prt"));
        List<FileUploadOutputs> fileUploadOutputsArrayList = fileUpload(fileNames);

        ScenarioIterationKey keyOne = fileUploadOutputsArrayList.get(0).getScenarioIterationKey();
        ScenarioIterationKey keyTwo = fileUploadOutputsArrayList.get(1).getScenarioIterationKey();

        keyOne.setIteration(3000000);
        keyTwo.setIteration(4000000);

        AcsResources acsResources = new AcsResources();

        ResponseWrapper<ScenariosInfoResponse> response = acsResources.getScenariosInformation(
                keyOne,
                keyTwo
        );

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getResponseEntity().isEmpty()).isEqualTo(true);
        softAssertions.assertAll();

    }

    @Test
    @TestRail(testCaseId = "10203")
    @Description("Negative Get Scenarios Info - Empty Body")
    public void negativeGetScenariosInfoEmptyBodyTest() {
        AcsResources acsResources = new AcsResources();

        ResponseWrapper<ScenariosInfoResponse> response = acsResources.getScenariosInfoNullBody();

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

        for (int i = 0; i < fileNames.size(); i++) {
            String processGroup = i == 0 ? ProcessGroupEnum.CASTING.getProcessGroup() : ProcessGroupEnum.SHEET_METAL.getProcessGroup();
            fileResponses.add(fileUploadResources.initializePartUpload(
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
     * @return List of GetScenariosInfoItems
     */
    private List<ScenariosInfoItem> coreGetScenariosInfoTest(List<String> fileNames) {
        List<FileUploadOutputs> fileUploadOutputs = fileUpload(fileNames);

        List<ScenarioIterationKey> scenarioIterationKeys = new ArrayList<>();

        for (FileUploadOutputs fileUploadOutput : fileUploadOutputs) {
            scenarioIterationKeys.add(fileUploadOutput.getScenarioIterationKey());
        }

        AcsResources acsResources = new AcsResources();
        ResponseWrapper<ScenariosInfoResponse> response = acsResources.getScenariosInformationOneScenario(scenarioIterationKeys);

        return new ArrayList<>(response.getResponseEntity());
    }

    /**
     * Performs assertions on response from get scenarios info endpoint
     *
     * @param scenarioItemsResponse - list of scenario items responses
     * @param fileNames - file names to assert against
     */
    private void getScenariosInfoAssertions(List<ScenariosInfoItem> scenarioItemsResponse, List<String> fileNames) {
        String userToExpect = "qa-automation";
        String componentTypeToExpect = "PART";
        String typeNameToExpect = "partState";

        SoftAssertions softAssertions = new SoftAssertions();

        for (int i = 0; i < scenarioItemsResponse.size(); i++) {
            ScenariosInfoItem scenariosInfoItem = scenarioItemsResponse.get(i);

            softAssertions.assertThat(scenariosInfoItem.getInitialized()).isFalse();
            softAssertions.assertThat(scenariosInfoItem.getMissing()).isFalse();
            softAssertions.assertThat(scenariosInfoItem.getVirtual()).isFalse();
            softAssertions.assertThat(scenariosInfoItem.getLocked()).isFalse();

            softAssertions.assertThat(scenariosInfoItem.getCreatedBy()).contains(userToExpect);
            softAssertions.assertThat(scenariosInfoItem.getUpdatedBy()).contains(userToExpect);

            softAssertions.assertThat(scenariosInfoItem.getComponentType()).isEqualTo(componentTypeToExpect);
            softAssertions.assertThat(scenariosInfoItem.getFileName()).isEqualTo(fileNames.get(i).toLowerCase());

            softAssertions.assertThat(scenariosInfoItem.getScenarioIterationKey().getScenarioKey().getTypeName()).isEqualTo(typeNameToExpect);
        }

        softAssertions.assertAll();
    }
}
