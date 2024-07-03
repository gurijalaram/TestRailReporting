package com.apriori.bcm.api.tests;

import static com.apriori.css.api.enums.CssSearch.SCENARIO_PUBLISHED_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.InputRowsGroupsResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CostWorksheetTests {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private String worksheetIdentity;
    private BcmUtil bcmUtil;
    private RequestEntityUtil requestEntityUtil;

    @BeforeEach
    public void setup() {
        requestEntityUtil = TestHelper.initUser();
        bcmUtil = new BcmUtil(requestEntityUtil);
    }

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null
            & bcmUtil.getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity().getStatus().equals("COMPLETE")) {
            bcmUtil.deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
        }
    }

    @Test
    @TestRail(id = {29815, 29816, 29817, 29973, 29976})
    @Description("Verify costing worksheet")
    public void costWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        WorkSheetResponse newWorksheet = bcmUtil.createWorksheet(name);
        worksheetIdentity = newWorksheet.getIdentity();
        WorkSheetResponse returnNewWorksheet = bcmUtil.getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(returnNewWorksheet.getStatus()).isEqualTo("NOT_STARTED");

        ScenarioItem cssComponentResponses = cssComponent
            .getBaseCssComponents(requestEntityUtil.getEmbeddedUser(), SCENARIO_PUBLISHED_EQ.getKey() + false, SCENARIO_STATE_EQ.getKey() + "NOT_COSTED").get(0);

        InputRowPostResponse responseWorksheetInputRow =
            bcmUtil.createWorkSheetInputRow(cssComponentResponses.getComponentIdentity(),
                cssComponentResponses.getScenarioIdentity(),
                worksheetIdentity).getResponseEntity();
        String inputRowIdentity = responseWorksheetInputRow.getIdentity();

        InputRowsGroupsResponse costWorksheet = bcmUtil.costWorksheet(InputRowsGroupsResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(costWorksheet.getSuccesses().get(0).getInputRowIdentity()).isEqualTo(inputRowIdentity);

        WorkSheetResponse returnWorksheet = bcmUtil.getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(returnWorksheet.getStatus()).containsAnyOf("IN_PROGRESS", "COMPLETE");
        softAssertions.assertThat(returnWorksheet.getAnalysisEvents()).isNotEmpty();

        String notExistingIdentity = "000000000000";
        ErrorResponse notExistingWorksheet = bcmUtil.costWorksheet(ErrorResponse.class, notExistingIdentity, HttpStatus.SC_NOT_FOUND).getResponseEntity();
        softAssertions.assertThat(notExistingWorksheet.getMessage())
            .isEqualTo(String.format("Worksheet: '%s' doesn't exists for a user.", notExistingIdentity));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {31006, 31007})
    @Description("Verify costing worksheet when one of the scenario is not in costable state or all input rows are not in costable state")
    public void costNotCostableState() {
        String name = GenerateStringUtil.saltString("name");
        WorkSheetResponse newWorksheet = bcmUtil.createWorksheet(name);
        worksheetIdentity = newWorksheet.getIdentity();

        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(requestEntityUtil.getEmbeddedUser(), "PART")
                .getResponseEntity().getItems().stream().filter(item -> item.getScenarioPublished().equals(true))
                .findFirst().orElse(null);

        String inputRowPublicIdentity = bcmUtil.createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(),
            worksheetIdentity).getResponseEntity().getIdentity();

        ErrorResponse publicWorksheetNotCosted = bcmUtil.costWorksheet(ErrorResponse.class, worksheetIdentity, HttpStatus.SC_BAD_REQUEST).getResponseEntity();

        softAssertions.assertThat(publicWorksheetNotCosted.getMessage())
            .isEqualTo("Unable to initiate costing for the worksheet, no scenarios in a costable state.");

        ScenarioItem cssComponentResponses = cssComponent
            .getBaseCssComponents(requestEntityUtil.getEmbeddedUser(), SCENARIO_PUBLISHED_EQ.getKey() + false, SCENARIO_STATE_EQ.getKey() + "NOT_COSTED").get(0);

        String inputRowPrivateIdentity = bcmUtil.createWorkSheetInputRow(cssComponentResponses.getComponentIdentity(),
            cssComponentResponses.getScenarioIdentity(),
            worksheetIdentity).getResponseEntity().getIdentity();

        InputRowsGroupsResponse costPublicAndPrivateWorksheet = bcmUtil.costWorksheet(InputRowsGroupsResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(costPublicAndPrivateWorksheet.getSuccesses().get(0).getInputRowIdentity()).isEqualTo(inputRowPrivateIdentity);
        softAssertions.assertThat(costPublicAndPrivateWorksheet.getFailures().get(0).getInputRowIdentity()).isEqualTo(inputRowPublicIdentity);
        softAssertions.assertThat(costPublicAndPrivateWorksheet.getFailures().get(0).getError()).contains("Could not initiate costing", "as it is either not costable or public");
        softAssertions.assertAll();
    }
}