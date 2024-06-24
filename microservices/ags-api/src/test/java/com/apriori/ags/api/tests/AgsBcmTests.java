package com.apriori.ags.api.tests;

import static com.apriori.css.api.enums.CssSearch.SCENARIO_PUBLISHED_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.ags.api.utils.AgsUtil;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.InputRowsGroupsResponse;
import com.apriori.bcm.api.models.response.MultipleInputRowsResponse;
import com.apriori.bcm.api.models.response.SuccessAddingAnalysisInputs;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.bcm.api.models.response.WorksheetGroupsResponse;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class AgsBcmTests extends AgsUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private String worksheetIdentity;
    private CssComponent cssComponent = new CssComponent();
    private final String componentType = "PART";

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            deleteWorksheet(worksheetIdentity);
        }
    }

    @Test
    @TestRail(id = {30735, 30736})
    @Description("Create worksheet and Get worksheet list")
    public void createWorksheetAndGetAList() {
        String name = new GenerateStringUtil().saltString("name");

        ResponseWrapper<WorkSheetResponse> response = createWorksheet(name);
        worksheetIdentity = response.getResponseEntity().getIdentity();
        softAssertions.assertThat(response.getResponseEntity().getName()).isEqualTo(name);

        WorkSheets worksheetsList = getWorksheetsWithParams("identity[IN]", worksheetIdentity).getResponseEntity();
        softAssertions.assertThat(worksheetsList.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        softAssertions.assertThat(worksheetsList.getItems().get(0).getIdentity()).isEqualTo(worksheetIdentity);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30738, 30739})
    @Description("Verify updating worksheet and get single worksheet")
    public void updateWorksheetTest() {
        String name = GenerateStringUtil.saltString("name");
        String updatedName = name + "updated";
        String description = new GenerateStringUtil().getRandomStringSpecLength(12);
        worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();

        WorkSheetResponse getSingleWorksheet = getWorksheet(worksheetIdentity).getResponseEntity();
        softAssertions.assertThat(getSingleWorksheet.getName()).isEqualTo(name);

        WorkSheetResponse updatedWorksheet = updateWorksheet(updatedName, description, worksheetIdentity).getResponseEntity();
        softAssertions.assertThat(updatedWorksheet.getName()).isEqualTo(updatedName);
        softAssertions.assertThat(updatedWorksheet.getDescription()).isEqualTo(description);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30741, 30742, 30744})
    @Description("Verify adding input rows to a worksheet")
    public void addInputRows() {
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().get(5);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        softAssertions.assertThat(responseWorksheetInputRow.getResponseEntity().getWorksheetId()).isNotNull();
        softAssertions.assertThat(responseWorksheetInputRow.getResponseEntity().getComponentIdentity())
            .isEqualTo(scenarioItem.getComponentIdentity());
        softAssertions.assertThat(responseWorksheetInputRow.getResponseEntity().getScenarioIdentity())
            .isEqualTo(scenarioItem.getScenarioIdentity());

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems())
            .isNotEmpty();

        List<ScenarioItem> scenarioItemList =
            new ArrayList<>(cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems());
        ScenarioItem scenario1 = scenarioItemList.get(0);
        ScenarioItem scenario2 = scenarioItemList.get(1);

        List<String> componentIdentityScenarioIdentity = Arrays.asList(scenario1.getComponentIdentity() + "," + scenario1.getScenarioIdentity(),
            scenario2.getComponentIdentity() + "," + scenario2.getScenarioIdentity());
        MultipleInputRowsResponse addRows =
            addMultipleInputRows(worksheetIdentity, componentIdentityScenarioIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(addRows.getSuccesses().get(0).getScenarioIdentity()).isEqualTo(scenario1.getScenarioIdentity());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30743, 30747})
    @Description("Verify Edit of public input row and delete input rows")
    public void editPublicInputRow() {
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream().filter(item -> item.getScenarioPublished().equals(true))
                .findFirst().orElse(null);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        String inputRowIdentity = createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            worksheetIdentity).getResponseEntity().getIdentity();

        InputRowsGroupsResponse editedRows =
            editInputRow(InputRowsGroupsResponse.class, worksheetIdentity, inputRowIdentity, HttpStatus.SC_OK).getResponseEntity();
        softAssertions.assertThat(editedRows.getSuccesses().get(0).getInputRowIdentity())
            .isEqualTo(inputRowIdentity);

        ResponseWrapper<InputRowsGroupsResponse> deletedInputRow =
            deleteInputRow(worksheetIdentity, inputRowIdentity);

        softAssertions.assertThat(deletedInputRow.getResponseEntity().getSuccesses().get(0).getInputRowIdentity())
            .isEqualTo(inputRowIdentity);

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems()).isEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30745})
    @Description("Verify setting inputs for an input row")
    public void settingCostingInputs() {
        String processGroupName = "Plastic Molding";
        String name = GenerateStringUtil.saltString("name");
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();
        worksheetIdentity = newWorksheet.getIdentity();
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().get(5);

        InputRowPostResponse responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity).getResponseEntity();
        String inputRowIdentity = responseWorksheetInputRow.getIdentity();

        SuccessAddingAnalysisInputs analysisInputsResponse = addAnalysisInputs(processGroupName, worksheetIdentity, inputRowIdentity).getResponseEntity();

        softAssertions.assertThat(analysisInputsResponse.getSuccesses().get(0).getAnalysisInput().getProcessGroupName()).isEqualTo(processGroupName);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 30746)
    @Description("Verify costing worksheet")
    public void costWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();
        String worksheetIdentity = newWorksheet.getIdentity();
        ScenarioItem cssComponentResponses = cssComponent
            .getBaseCssComponents(testingUser, SCENARIO_PUBLISHED_EQ.getKey() + false, SCENARIO_STATE_EQ.getKey() + "NOT_COSTED").get(0);

        InputRowPostResponse responseWorksheetInputRow =
            createWorkSheetInputRow(cssComponentResponses.getComponentIdentity(),
                cssComponentResponses.getScenarioIdentity(),
                worksheetIdentity).getResponseEntity();
        String inputRowIdentity = responseWorksheetInputRow.getIdentity();

        InputRowsGroupsResponse costWorksheet = costWorksheet(InputRowsGroupsResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(costWorksheet.getSuccesses().get(0).getInputRowIdentity()).isEqualTo(inputRowIdentity);

        WorkSheetResponse returnWorksheet = getWorksheet(worksheetIdentity).getResponseEntity();

        softAssertions.assertThat(returnWorksheet.getStatus()).containsAnyOf("IN_PROGRESS", "COMPLETE");
        softAssertions.assertThat(returnWorksheet.getAnalysisEvents()).isNotEmpty();
        softAssertions.assertAll();

        if (worksheetIdentity != null
            & getWorksheet(worksheetIdentity).getResponseEntity().getStatus().equals("COMPLETE")) {
            deleteWorksheet(worksheetIdentity);
        }
    }

    @Test
    @TestRail(id = 30748)
    @Description("Get a filtered and sorted list of scenario iteration candidates")
    public void getWorksheetCandidates() {
        String name = new GenerateStringUtil().saltString("name");

        worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();

        ComponentResponse getFilteredCandidates = getCandidatesWithParams(worksheetIdentity, "componentType[IN]", "PART").getResponseEntity();
        softAssertions.assertThat(getFilteredCandidates.getItems().get(0).getComponentType()).isEqualTo("PART");

        ComponentResponse getSortedCandidates = getCandidatesWithParams(worksheetIdentity, "sortBy[DESC]", "scenarioCreatedAt").getResponseEntity();

        softAssertions.assertThat(getSortedCandidates.getItems()).isNotEmpty();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30740})
    @Description("Verify multiple delete worksheet endpoint")
    public void deleteMultipleWorksheets() {
        String name1 = GenerateStringUtil.saltString("name1");
        String name2 = GenerateStringUtil.saltString("name2");
        String worksheetIdentity1 = createWorksheet(name1).getResponseEntity().getIdentity();
        String worksheetIdentity2 = createWorksheet(name2).getResponseEntity().getIdentity();

        WorksheetGroupsResponse multipleDelete = deleteMultipleWorksheets(
            WorksheetGroupsResponse.class, HttpStatus.SC_OK, worksheetIdentity1, worksheetIdentity2).getResponseEntity();

        softAssertions.assertThat(multipleDelete.getSuccesses().get(0).getIdentity()).isEqualTo(worksheetIdentity1);
        softAssertions.assertThat(multipleDelete.getSuccesses().get(1).getIdentity()).isEqualTo(worksheetIdentity2);
        softAssertions.assertAll();
    }
}