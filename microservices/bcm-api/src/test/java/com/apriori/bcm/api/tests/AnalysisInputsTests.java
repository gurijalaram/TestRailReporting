package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.FailureAddingAnalysisInputs;
import com.apriori.bcm.api.models.response.SuccessAddingAnalysisInputs;
import com.apriori.bcm.api.models.response.WorkSheetInputRowResponse;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class AnalysisInputsTests extends BcmUtil {
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final CssComponent cssComponent = new CssComponent();
    private final String componentType = "PART";
    private final String processGroupName = "Sheet Metal";

    // TODO - add clean up method once API will be available

    @Test
    @TestRail(id = {29672})
    @Description("Verify setting inputs for an input row")
    public void settingInputs() {
        String name = GenerateStringUtil.saltString("name");
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();
        String worksheetIdentity = newWorksheet.getIdentity();
        ScenarioItem scenarioItem = cssComponent.postSearchRequest(testingUser, componentType).getResponseEntity().getItems().stream()
            .findFirst().orElse(null);

        WorkSheetInputRowResponse responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity).getResponseEntity();
        String inputRowIdentity = responseWorksheetInputRow.getIdentity();

        SuccessAddingAnalysisInputs analysisInputsResponse = addAnalysisInputs(SuccessAddingAnalysisInputs.class, processGroupName, worksheetIdentity, HttpStatus.SC_OK, inputRowIdentity).getResponseEntity();

        softAssertions.assertThat(analysisInputsResponse.getSuccesses().get(0).getAnalysisInput().getProcessGroupName()).isEqualTo(processGroupName);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29673, 29674, 29675})
    @Description("Verify negative cases of adding analysis inputs")
    public void settingInputsNegativeTests() {
        String notExistingIdentity = "000000000000";
        WorkSheetResponse existingWorksheet = getWorksheets().getResponseEntity().getItems().get(0);
        String worksheetIdentity = existingWorksheet.getIdentity();

        ErrorResponse invalidWorksheetId = addAnalysisInputs(ErrorResponse.class, processGroupName, "0000000", HttpStatus.SC_BAD_REQUEST, notExistingIdentity).getResponseEntity();
        softAssertions.assertThat(invalidWorksheetId.getMessage()).isEqualTo("'identity' is not a valid identity.");

        ErrorResponse invalidInputRowId = addAnalysisInputs(ErrorResponse.class, processGroupName, worksheetIdentity, HttpStatus.SC_BAD_REQUEST, "0000000").getResponseEntity();
        softAssertions.assertThat(invalidInputRowId.getMessage()).isEqualTo("'inputRowIdentity' is not a valid identity.");

        ErrorResponse withoutInputRowId = addAnalysisInputs(ErrorResponse.class, processGroupName, worksheetIdentity, HttpStatus.SC_BAD_REQUEST, null).getResponseEntity();
        softAssertions.assertThat(withoutInputRowId.getMessage()).isEqualTo("'inputRowIdentity' should not be null.");

        ErrorResponse notExistingWorksheet = addAnalysisInputs(ErrorResponse.class, processGroupName, notExistingIdentity, HttpStatus.SC_NOT_FOUND, notExistingIdentity).getResponseEntity();
        softAssertions.assertThat(notExistingWorksheet.getMessage()).isEqualTo(String.format("Resource 'Worksheet' with identity '%s' was not found", notExistingIdentity));

        FailureAddingAnalysisInputs notRelatedInputRows = addAnalysisInputs(FailureAddingAnalysisInputs.class, processGroupName, worksheetIdentity, HttpStatus.SC_OK, notExistingIdentity).getResponseEntity();
        softAssertions.assertThat(notRelatedInputRows.getFailures().get(0).getError()).isEqualTo(String.format("Input Row with Identity: '%s'  does not exist in Worksheet with Identity: '%s'", notExistingIdentity, worksheetIdentity));

        softAssertions.assertAll();
    }
}