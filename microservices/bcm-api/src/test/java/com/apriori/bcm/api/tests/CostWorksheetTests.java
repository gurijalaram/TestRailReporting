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
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CostWorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();
    private String worksheetIdentity;

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null
            & getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity().getStatus().equals("COMPLETE")) {
            deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
        }
    }

    @Test
    @TestRail(id = {29815, 29816, 29817, 29973, 29976})
    @Description("Verify costing worksheet")
    public void costWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();
        worksheetIdentity = newWorksheet.getIdentity();
        WorkSheetResponse returnNewWorksheet = getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(returnNewWorksheet.getStatus()).isEqualTo("NOT_STARTED");

        ScenarioItem cssComponentResponses = cssComponent
            .getBaseCssComponents(testingUser, SCENARIO_PUBLISHED_EQ.getKey() + false, SCENARIO_STATE_EQ.getKey() + "NOT_COSTED").get(0);

        InputRowPostResponse responseWorksheetInputRow =
            createWorkSheetInputRow(cssComponentResponses.getComponentIdentity(),
                cssComponentResponses.getScenarioIdentity(),
                worksheetIdentity).getResponseEntity();
        String inputRowIdentity = responseWorksheetInputRow.getIdentity();

        InputRowsGroupsResponse costWorksheet = costWorksheet(InputRowsGroupsResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(costWorksheet.getSuccesses().get(0).getInputRowIdentity()).isEqualTo(inputRowIdentity);

        WorkSheetResponse returnWorksheet = getWorksheet(WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(returnWorksheet.getStatus()).containsAnyOf("IN_PROGRESS", "COMPLETE");
        softAssertions.assertThat(returnWorksheet.getAnalysisEvents()).isNotEmpty();

        String notExistingIdentity = "000000000000";
        ErrorResponse notExistingWorksheet = costWorksheet(ErrorResponse.class, notExistingIdentity, HttpStatus.SC_NOT_FOUND).getResponseEntity();
        softAssertions.assertThat(notExistingWorksheet.getMessage())
            .isEqualTo(String.format("Worksheet: '%s' doesn't exists for a user.", notExistingIdentity));
        softAssertions.assertAll();
    }
}