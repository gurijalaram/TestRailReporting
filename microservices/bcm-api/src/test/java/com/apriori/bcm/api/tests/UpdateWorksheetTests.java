package com.apriori.bcm.api.tests;

import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.InputRowsGroupsResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorksheetGroupsResponse;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class UpdateWorksheetTests extends BcmUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private final String componentType = "PART";
    private CssComponent cssComponent = new CssComponent();
    private String worksheetIdentity;
    private String worksheetIdentity2;

    @AfterEach
    public void cleanUp() {
        if (worksheetIdentity != null) {
            deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
        }
        if (worksheetIdentity2 != null) {
            deleteWorksheet(null, worksheetIdentity2, HttpStatus.SC_NO_CONTENT);
        }
    }

    @Test
    @TestRail(id = {29487})
    @Description("Verify updating a Bulk Analysis worksheet")
    public void updateWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        String updatedName = name + "updated";
        worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();

        WorkSheetResponse updatedWorksheet = updateWorksheet(updatedName, null, WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

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
        worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();

        ErrorResponse sameName = updateWorksheet(existingNameWorksheet, null, ErrorResponse.class, worksheetIdentity, HttpStatus.SC_CONFLICT).getResponseEntity();

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
        worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();


        WorkSheetResponse updatedWorksheet = updateWorksheet(null, description, WorkSheetResponse.class, worksheetIdentity, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(updatedWorksheet.getDescription()).isEqualTo(description);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29567, 29839})
    @Description("Verify delete input row")
    public void deleteInputRow() {
        ScenarioItem scenarioItem =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        ResponseWrapper<InputRowsGroupsResponse> deletedInputRow =
            deleteInputRow(InputRowsGroupsResponse.class, worksheetIdentity,
                responseWorksheetInputRow.getResponseEntity().getIdentity(), HttpStatus.SC_OK);

        softAssertions.assertThat(deletedInputRow.getResponseEntity().getSuccesses().get(0).getInputRowIdentity())
            .isEqualTo(responseWorksheetInputRow.getResponseEntity().getIdentity());

        ResponseWrapper<WorkSheetInputRowGetResponse> worksheetRow =
            getWorkSheetInputRow(worksheetIdentity);

        softAssertions.assertThat(worksheetRow.getResponseEntity().getItems()).isEmpty();
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

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow =
            createWorkSheetInputRow(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(),
                worksheetIdentity);

        ResponseWrapper<ErrorResponse> deletedInputRow =
            deleteInputRow(ErrorResponse.class, "fakeIdentity",
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

        worksheetIdentity = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow1 =
            createWorkSheetInputRow(scenarioItem1.getComponentIdentity(),
                scenarioItem1.getScenarioIdentity(),
                worksheetIdentity);

        ScenarioItem scenarioItem2 =
            cssComponent.postSearchRequest(testingUser, componentType)
                .getResponseEntity().getItems().stream()
                .findFirst().orElse(null);

        worksheetIdentity2 = createWorksheet(GenerateStringUtil.saltString("name"))
            .getResponseEntity()
            .getIdentity();

        ResponseWrapper<InputRowPostResponse> responseWorksheetInputRow2 =
            createWorkSheetInputRow(scenarioItem2.getComponentIdentity(),
                scenarioItem2.getScenarioIdentity(),
                worksheetIdentity2);

        ResponseWrapper<InputRowsGroupsResponse> deletedInputRow =
            deleteInputRow(InputRowsGroupsResponse.class, worksheetIdentity,
                responseWorksheetInputRow2.getResponseEntity().getIdentity(), HttpStatus.SC_OK);

        softAssertions.assertThat(deletedInputRow.getResponseEntity().getFailures().get(0).getError())
            .isEqualTo(String.format("Input Row with Identity: '%s'  does not exist in Worksheet with Identity: '%s'",
                responseWorksheetInputRow2.getResponseEntity().getIdentity(), worksheetIdentity));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29834, 29838})
    @Description("Verify delete a single worksheet")
    public void deleteWorksheet() {
        String name = GenerateStringUtil.saltString("name");
        String worksheetIdentity = createWorksheet(name).getResponseEntity().getIdentity();

        deleteWorksheet(null, worksheetIdentity, HttpStatus.SC_NO_CONTENT);
        ErrorResponse deletedWorksheet = getWorksheet(ErrorResponse.class, worksheetIdentity, HttpStatus.SC_NOT_FOUND).getResponseEntity();

        softAssertions.assertThat(deletedWorksheet.getMessage())
            .isEqualTo(String.format("Resource 'Worksheet' with identity '%s' was not found", worksheetIdentity));

        ErrorResponse deleteAlreadyDeleted = deleteWorksheet(ErrorResponse.class, worksheetIdentity, HttpStatus.SC_NOT_FOUND).getResponseEntity();
        softAssertions.assertThat(deleteAlreadyDeleted.getMessage())
            .isEqualTo(String.format("Resource 'Worksheet' with identity '%s' was not found", worksheetIdentity));

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {29837})
    @Description("Verify deleting the invalid worksheet")
    public void deleteInvalidWorksheet() {
        ErrorResponse noIdentity = deleteWorksheet(ErrorResponse.class, null, HttpStatus.SC_BAD_REQUEST).getResponseEntity();

        softAssertions.assertThat(noIdentity.getMessage())
            .isEqualTo("'identity' is not a valid identity.");

        ErrorResponse invalidIdentity = deleteWorksheet(ErrorResponse.class, "0000000", HttpStatus.SC_BAD_REQUEST).getResponseEntity();

        softAssertions.assertThat(invalidIdentity.getMessage())
            .isEqualTo("'identity' is not a valid identity.");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {30653, 30654, 30655})
    @Description("Verify multiple delete worksheet endpoint")
    public void deleteMultipleWorksheets() {
        String name1 = GenerateStringUtil.saltString("name1");
        String name2 = GenerateStringUtil.saltString("name2");
        String worksheetIdentity1 = createWorksheet(name1).getResponseEntity().getIdentity();
        String worksheetIdentity2 = createWorksheet(name2).getResponseEntity().getIdentity();

        WorksheetGroupsResponse multipleDelete = deleteMultipleWorksheets(
            WorksheetGroupsResponse.class, worksheetIdentity1, worksheetIdentity2, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(multipleDelete.getSuccesses().get(0).getIdentity()).isEqualTo(worksheetIdentity1);
        softAssertions.assertThat(multipleDelete.getSuccesses().get(1).getIdentity()).isEqualTo(worksheetIdentity2);

        WorksheetGroupsResponse deleteAlreadyDeletedWorksheets = deleteMultipleWorksheets(
            WorksheetGroupsResponse.class, worksheetIdentity1, worksheetIdentity2, HttpStatus.SC_OK).getResponseEntity();

        softAssertions.assertThat(deleteAlreadyDeletedWorksheets.getFailures().get(0).getError()).contains("Resource 'Worksheet'", "was not found");
        ErrorResponse deleteInvalidWorksheets = deleteMultipleWorksheets(
            ErrorResponse.class, "0000000", null, HttpStatus.SC_BAD_REQUEST).getResponseEntity();

        softAssertions.assertThat(deleteInvalidWorksheets.getMessage())
            .isEqualTo("2 validation failures were found:\n* 'groupItem[0].identity' is not a valid identity.\n* 'groupItem[1].identity' should not be null.");
        softAssertions.assertAll();
    }
}