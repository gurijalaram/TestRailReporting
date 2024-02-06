package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.InputRowDeleted;
import com.apriori.bcm.api.models.response.WorkSheetInputRowResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.utils.BcmUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UpdateWorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private final BcmUtil bcmUtil = new BcmUtil();
    private final String componentType = "PART";
    private CssComponent cssComponent = new CssComponent();

    // TODO - add clean up method once API will be available

    @Test
    @TestRail(id = {29487})
    @Description("Verify updating a Bulk Analysis worksheet")
    public void updateWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        String updatedName = name + "updated";
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();

        WorkSheetResponse updatedWorksheet = updateWorksheet(updatedName, null, WorkSheetResponse.class, newWorksheet.getIdentity(), HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(updatedWorksheet.getName()).isEqualTo(updatedName);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29694})
    @Description("Verify negative cases of updating a Bulk Analysis worksheet")
    public void updateWorksheetNegativeTests() {
        String name = GenerateStringUtil.saltString("name");
        String updatedName = name + "updated";
        WorkSheetResponse existingWorksheet = getWorksheets().getResponseEntity().getItems().get(0);
        String existingNameWorksheet = existingWorksheet.getName();
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();
        String worksheetId = newWorksheet.getIdentity();

        ErrorResponse sameName = updateWorksheet(existingNameWorksheet, null, ErrorResponse.class, worksheetId, HttpStatus.SC_CONFLICT).getResponseEntity();

        softAssertions.assertThat(sameName.getMessage()).contains("already exists");

        ErrorResponse invalidIdentity = updateWorksheet(updatedName, null, ErrorResponse.class, "0000000", HttpStatus.SC_BAD_REQUEST).getResponseEntity();
        softAssertions.assertThat(invalidIdentity.getMessage()).isEqualTo("'identity' is not a valid identity.");

        ErrorResponse notExistingWorksheet = updateWorksheet(updatedName, null, ErrorResponse.class, "000000000000", HttpStatus.SC_NOT_FOUND).getResponseEntity();
        softAssertions.assertThat(notExistingWorksheet.getMessage()).contains("was not found");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29695})
    @Description("Verify updating worksheet with a description")
    public void updateWorksheetWithDescription() {
        String name = GenerateStringUtil.saltString("name");
        String description = new GenerateStringUtil().getRandomString();
        WorkSheetResponse newWorksheet = createWorksheet(name).getResponseEntity();

        WorkSheetResponse updatedWorksheet = updateWorksheet(null, description, WorkSheetResponse.class, newWorksheet.getIdentity(), HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(updatedWorksheet.getDescription()).isEqualTo(description);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29567})
    @Description("Verify delete input row")
    public void deleteInputRow() {
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        String worksheetIdentity = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowResponse> responseWorksheetInputRow =
            bcmUtil.createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        ResponseWrapper<InputRowDeleted> deletedInputRow =
            bcmUtil.deleteInputRow(InputRowDeleted.class, worksheetIdentity,
                responseWorksheetInputRow.getResponseEntity().getIdentity(), HttpStatus.SC_OK);

        softAssertions.assertThat(deletedInputRow.getResponseEntity().getSuccesses().get(0).getInputRowIdentity())
            .isEqualTo(responseWorksheetInputRow.getResponseEntity().getIdentity());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29568})
    @Description("Verify delete input row with  invalid worksheet identity ")
    public void deleteInputRowInvalidIdentity() {

        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        String worksheetIdentity = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowResponse> responseWorksheetInputRow =
            bcmUtil.createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        ResponseWrapper<ErrorResponse> deletedInputRow =
            bcmUtil.deleteInputRow(ErrorResponse.class, "fakeIdentity",
                responseWorksheetInputRow.getResponseEntity().getIdentity(), HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(deletedInputRow.getResponseEntity().getMessage())
            .isEqualTo("Resource 'Worksheet' with identity 'fakeIdentity' was not found");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29569})
    @Description("Verify delete input row which does not belong to its worksheet")
    public void deleteInputRowForWrongWorksheet() {

        ScenarioItem scenarioItem1 =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        String worksheetIdentity1 = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowResponse> responseWorksheetInputRow1 =
            bcmUtil.createWorkSheetInputRow(scenarioItem1.getComponentIdentity(),
                scenarioItem1.getScenarioIdentity(),
                worksheetIdentity1);

        ScenarioItem scenarioItem2 =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        String worksheetIdentity2 = bcmUtil.createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<WorkSheetInputRowResponse> responseWorksheetInputRow2 =
            bcmUtil.createWorkSheetInputRow(scenarioItem2.getComponentIdentity(),
                scenarioItem2.getScenarioIdentity(),
                worksheetIdentity2);

        ResponseWrapper<InputRowDeleted> deletedInputRow =
            bcmUtil.deleteInputRow(InputRowDeleted.class, worksheetIdentity1,
                responseWorksheetInputRow2.getResponseEntity().getIdentity(), HttpStatus.SC_OK);

        softAssertions.assertThat(deletedInputRow.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo(String.format("Input Row with Identity: '%s'  does not exist in Worksheet with Identity: '%s'",
                responseWorksheetInputRow2.getResponseEntity().getIdentity(), worksheetIdentity1));

        softAssertions.assertAll();
    }
}